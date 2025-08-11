package com.sesac.userservice.service;

import com.sesac.userservice.dto.LoginRequest;
import com.sesac.userservice.dto.LoginResponse;
import com.sesac.userservice.entity.User;
import com.sesac.userservice.repository.UserRepository;
import com.sesac.userservice.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider jwtTokenProvider;

  public User findById(Long id) {
      return userRepository.findById(id).orElseThrow(
              () -> new RuntimeException(("User not found with id: "+id))
      );
  }

  public LoginResponse login(LoginRequest request) {
      // 1. Identity(이메일)로 사용자 조회
      User user = userRepository.findByEmail(request.getEmail())
              .orElseThrow(() -> new RuntimeException(("invalid Email")));
      // 2. 패스워드 검증 , DB저장시 비문으로 하므로 password encoder추가
      if (!passwordEncoder.matches(request.getPassword(), user.getPassword())){
          throw new RuntimeException("invalid email or password");
      }// Encoding된 패스워드를 받아서 DB의 패스워드와 비교가능
      // 3. JWT 토큰 생성
      String token= jwtTokenProvider.generateToken(user.getEmail(), user.getId());
      // 4. 응답 생성
      return new LoginResponse(token, user.getId(),user.getEmail(), user.getName());
  }
}
