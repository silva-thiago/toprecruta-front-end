package br.com.topsolutions.service.impl;

import br.com.topsolutions.dto.request.CreateUserRequest;
import br.com.topsolutions.dto.request.UpdateUserRequest;
import br.com.topsolutions.dto.response.PageResponse;
import br.com.topsolutions.dto.response.UserResponse;
import br.com.topsolutions.entity.User;
import br.com.topsolutions.exception.BusinessException;
import br.com.topsolutions.exception.ResourceNotFoundException;
import br.com.topsolutions.repository.UserRepository;
import br.com.topsolutions.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  @Transactional(readOnly = true)
  public PageResponse<UserResponse> findAll(String search, Boolean active, int page, int size, String sort) {
    var direction = sort.startsWith("-") ? Sort.Direction.DESC : Sort.Direction.ASC;
    var field = sort.startsWith("-") ? sort.substring(1) : sort;
    var pageable = PageRequest.of(page, size, Sort.by(direction, field));
    return PageResponse.from(
      userRepository.findAllFiltered(
        (search == null || search.isBlank()) ? null : search, active, pageable
      ).map(UserResponse::from)
    );
  }

  @Override
  @Transactional(readOnly = true)
  public UserResponse findById(String id) {
    return UserResponse.from(findUserOrThrow(id));
  }

  @Override
  @Transactional
  public UserResponse create(CreateUserRequest req) {
    if (userRepository.existsByEmail(req.email())) {
      throw new BusinessException("Email ja cadastrado: " + req.email());
    }
    var user = User.builder()
      .name(req.name())
      .email(req.email())
      .password(passwordEncoder.encode(req.password()))
      .birthDate(req.birthDate())
      .phoneNumber(req.phoneNumber())
      .role(req.role() != null ? req.role() : User.Role.USER)
      .build();
    return UserResponse.from(userRepository.save(user));
  }

  @Override
  @Transactional
  public UserResponse update(String id, UpdateUserRequest req) {
    var user = findUserOrThrow(id);

    if (req.name() != null) user.setName(req.name());
    if (req.birthDate() != null) user.setBirthDate(req.birthDate());
    if (req.phoneNumber() != null) user.setPhoneNumber(req.phoneNumber());
    if (req.role() != null) user.setRole(req.role());
    if (req.active() != null) user.setActive(req.active());

    if (req.email() != null && !req.email().equals(user.getEmail())) {
      if (userRepository.existsByEmail(req.email())) {
        throw new BusinessException("Email ja cadastrado: " + req.email());
      }
      user.setEmail(req.email());
    }

    if (req.password() != null && !req.password().isBlank()) {
      user.setPassword(passwordEncoder.encode(req.password()));
    }

    return UserResponse.from(userRepository.save(user));
  }

  @Override
  @Transactional
  public void delete(String id) {
    var user = findUserOrThrow(id);
    // soft delete: apenas desativa
    user.setActive(false);
    userRepository.save(user);
  }

  private User findUserOrThrow(String id) {
    return userRepository.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Usuario nao encontrado: " + id));
  }
}
