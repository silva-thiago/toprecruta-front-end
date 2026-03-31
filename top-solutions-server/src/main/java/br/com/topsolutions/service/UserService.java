package br.com.topsolutions.service;

import br.com.topsolutions.dto.request.CreateUserRequest;
import br.com.topsolutions.dto.request.UpdateUserRequest;
import br.com.topsolutions.dto.response.PageResponse;
import br.com.topsolutions.dto.response.UserResponse;

public interface UserService {
  PageResponse<UserResponse> findAll(String search, Boolean active, int page, int size, String sort);

  UserResponse findById(String id);

  UserResponse create(CreateUserRequest request);

  UserResponse update(String id, UpdateUserRequest request);

  void delete(String id);
}
