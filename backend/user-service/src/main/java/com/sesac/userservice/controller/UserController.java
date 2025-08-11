package com.sesac.userservice.controller;

import com.sesac.userservice.dto.LoginRequest;
import com.sesac.userservice.dto.LoginResponse;
import com.sesac.userservice.entity.User;
import com.sesac.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    @Operation(summary="사용자 조회", description="ID로 사용자 정보조회합니다.")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        try{
            User user = userService.findById(id);
            return ResponseEntity.ok(user);
        }catch(RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/login")
    @Operation(summary="로그인", description="이메일과 패스워드로 로그인후 JWT토큰발급")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request){
        try {
            LoginResponse response = userService.login(request);
            return ResponseEntity.ok(response);
        }catch(RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
