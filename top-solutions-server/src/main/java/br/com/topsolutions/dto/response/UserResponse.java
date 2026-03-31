package br.com.topsolutions.dto.response;

import br.com.topsolutions.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record UserResponse(
  String id,
  String name,
  String email,
  LocalDate birthDate,
  String phoneNumber,
  User.Role role,
  Boolean active,
  LocalDateTime createdAt,
  LocalDateTime updatedAt
) {
  public static UserResponse from(User user) {
    return new UserResponse(
      user.getId(),
      user.getName(),
      user.getEmail(),
      user.getBirthDate(),
      user.getPhoneNumber(),
      user.getRole(),
      user.getActive(),
      user.getCreatedAt(),
      user.getUpdatedAt()
    );
  }
}
