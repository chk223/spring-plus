package org.example.expert.domain.todo.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.manager.entity.QManager;
import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.entity.QTodo;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class TodoRepositoryImpl implements TodoRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Todo> findByIdWithUser(Long todoId) {
        QTodo todo = QTodo.todo;
        return Optional.ofNullable(queryFactory
                .selectFrom(todo)
                .leftJoin(todo.user)
                .fetchJoin()
                .where(todo.id.eq(todoId))
                .fetchOne());
    }

    @Override
    public Page<TodoSearchResponse> findBySearch(String searchKeyword, String managerNickname, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        QTodo todo = QTodo.todo;
        QManager manager = QManager.manager;
        BooleanBuilder builder = getConditionsForSearch(searchKeyword, managerNickname, startDate, endDate, todo);

        List<TodoSearchResponse> content = getContent(pageable, todo, manager, builder);

        Long totalCount = getTotalCount(todo, builder);

        long total = totalCount == null ? 0L : totalCount;

        return new PageImpl<>(content, pageable, total);
    }

    private List<TodoSearchResponse> getContent(Pageable pageable, QTodo todo, QManager manager, BooleanBuilder builder) {
        return queryFactory
                .select(Projections.constructor(TodoSearchResponse.class,
                        todo.title,
                        todo.managers.size(),
                        todo.comments.size()))
                .from(todo)
                .leftJoin(todo.managers, manager)
                .where(builder)
                .orderBy(todo.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private Long getTotalCount(QTodo todo, BooleanBuilder builder) {
        return queryFactory
                .select(todo.count())
                .from(todo)
                .where(builder)
                .fetchOne();
    }

    private static BooleanBuilder getConditionsForSearch(String searchKeyword, String managerNickname, LocalDateTime startDate, LocalDateTime endDate, QTodo todo) {
        BooleanBuilder builder = new BooleanBuilder();

        if (StringUtils.hasText(searchKeyword)) {
            builder.and(todo.title.contains(searchKeyword));
        }

        if (startDate != null) {
            builder.and(todo.createdAt.goe(startDate));
        }

        if (endDate != null) {
            builder.and(todo.createdAt.loe(endDate));
        }

        if (StringUtils.hasText(managerNickname)) {
            builder.and(todo.managers.any().user.nickname.contains(managerNickname));
        }
        return builder;
    }
}
