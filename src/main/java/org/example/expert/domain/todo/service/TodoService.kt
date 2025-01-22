package org.example.expert.domain.todo.service

import org.example.expert.client.WeatherClient
import org.example.expert.domain.common.dto.AuthUser
import org.example.expert.domain.common.exception.InvalidRequestException
import org.example.expert.domain.todo.dto.request.TodoSaveRequest
import org.example.expert.domain.todo.dto.response.TodoResponse
import org.example.expert.domain.todo.dto.response.TodoSaveResponse
import org.example.expert.domain.todo.dto.response.TodoSearchResponse
import org.example.expert.domain.todo.entity.Todo
import org.example.expert.domain.todo.repository.TodoRepository
import org.example.expert.domain.user.dto.response.UserResponse
import org.example.expert.domain.user.entity.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class TodoService(
    private val todoRepository: TodoRepository,
    private val weatherClient: WeatherClient
) {

    @Transactional
    fun saveTodo(authUser: AuthUser, todoSaveRequest: TodoSaveRequest): TodoSaveResponse {
        val user = User.fromAuthUser(authUser)

        val weather = weatherClient.todayWeather

        val newTodo = Todo(
            title = todoSaveRequest.title!!,//!! -> null 이 아님을 명시적으로 선언, null 시 에러.
            contents = todoSaveRequest.contents!!,
            weather = weather,
            user = user
        )
        val savedTodo = todoRepository.save(newTodo)

        return savedTodo.id?.let { id ->
            TodoSaveResponse(
                id = id,
                title = savedTodo.title,
                contents = savedTodo.contents,
                weather = weather,
                user =UserResponse(user.id, user.email)
            )
        } ?: throw InvalidRequestException("Todo id cannot be null")
    }

    @Transactional(readOnly = true)
    fun getTodos(
        page: Int,
        size: Int,
        weather: String? = null,
        startDate: LocalDateTime? = null,
        endDate: LocalDateTime? = null
    ): Page<TodoResponse> {
        val pageable = PageRequest.of(page - 1, size)

        val todos = if ((weather == null && startDate == null && endDate == null))
            todoRepository.findAllByOrderByModifiedAtDesc(pageable)
        else
            todoRepository.findByConditions(weather, startDate, endDate, pageable)
        return todos.map { todo ->
            todo.id?.let { id ->
                TodoResponse(
                    id = id,
                    title = todo.title,
                    contents = todo.contents,
                    weather = todo.weather,
                    user = UserResponse(todo.user.id, todo.user.email),
                    createdAt = todo.createdAt,
                    modifiedAt = todo.modifiedAt
                )
            } ?: throw InvalidRequestException("Todo id cannot be null")
        }
    }

    @Transactional(readOnly = true)
    fun getTodo(todoId: Long): TodoResponse {
        val todo = todoRepository.findByIdWithUser(todoId)
            .orElseThrow { InvalidRequestException("Todo not found") }

        return todo.id?.let { id ->
            TodoResponse(
                id = id,
                title = todo.title,
                contents = todo.contents,
                weather = todo.weather,
                user = UserResponse(todo.user.id, todo.user.email),
                createdAt = todo.createdAt,
                modifiedAt = todo.modifiedAt
            )
        } ?: throw InvalidRequestException("Todo id cannot be null")
    }

    fun searchTodo(
        page: Int,
        size: Int,
        searchKeyword: String? = null,
        startDate: LocalDateTime? = null,
        endDate: LocalDateTime? = null,
        managerNickname: String? = null
    ): Page<TodoSearchResponse> {
        val pageable = PageRequest.of(page - 1, size)
        return todoRepository.findBySearch(searchKeyword, managerNickname, startDate, endDate, pageable)
    }
}
