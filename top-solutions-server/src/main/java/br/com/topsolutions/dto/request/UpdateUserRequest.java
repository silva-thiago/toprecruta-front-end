package br.com.topsolutions.dto.request;

import br.com.topsolutions.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UpdateUserRequest(
  String name,
  @Email String email,
  @Size(min = 6) String password,
  LocalDate birthDate,
  String phoneNumber,
  User.Role role,
  Boolean active
) {
}
