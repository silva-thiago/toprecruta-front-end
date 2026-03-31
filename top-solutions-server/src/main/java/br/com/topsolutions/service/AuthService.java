package br.com.topsolutions.service;

import br.com.topsolutions.dto.request.LoginRequest;
import br.com.topsolutions.dto.response.AuthResponse;

public interface AuthService {
  AuthResponse login(LoginRequest request);
}
