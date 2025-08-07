package com.sesac.userservice.service;

import com.sesac.userservice.entity.User;
import com.sesac.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
  public final UserRepository userRepository;
  public User findById(Long id) {
      return userRepository.findById(id).orElseThrow(
              () -> new RuntimeException(("User not found with id: "+id))
      );
  }
}
