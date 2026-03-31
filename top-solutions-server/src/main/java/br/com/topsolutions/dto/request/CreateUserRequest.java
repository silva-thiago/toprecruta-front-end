package br.com.topsolutions.dto.request;

import br.com.topsolutions.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CreateUserRequest(
  @NotBlank String name,
  @NotBlank @Email String email,
  @NotBlank @Size(min = 6) String password,
  LocalDate birthDate,
  String phoneNumber,
  User.Role role
) {
}
