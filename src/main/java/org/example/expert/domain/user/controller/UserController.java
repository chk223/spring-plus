package org.example.expert.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.common.annotation.Auth;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.user.dto.request.UserChangePasswordRequest;
import org.example.expert.domain.user.dto.request.UserNicknameChangeRequest;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable long userId) {
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @GetMapping("/nick/{nickName}")
    public ResponseEntity<UserResponse> getUserByNickname(@PathVariable String nickName) {
        return ResponseEntity.ok(userService.getUserByNickname(nickName));
    }
    @PutMapping("/nickname")
    public void changeNickname(@Auth AuthUser authUser, @RequestBody UserNicknameChangeRequest userNicknameChangeRequest) {
        userService.changeNickname(authUser.getId(), userNicknameChangeRequest);
    }

    @PutMapping("/password")
    public void changePassword(@Auth AuthUser authUser, @RequestBody UserChangePasswordRequest userChangePasswordRequest) {
        userService.changePassword(authUser.getId(), userChangePasswordRequest);
    }
}
