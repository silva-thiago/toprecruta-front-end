package br.com.topsolutions.service.impl;

import br.com.topsolutions.dto.request.LoginRequest;
import br.com.topsolutions.dto.response.AuthResponse;
import br.com.topsolutions.dto.response.UserResponse;
import br.com.topsolutions.repository.UserRepository;
import br.com.topsolutions.security.JwtService;
import br.com.topsolutions.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final AuthenticationManager authManager;
  private final JwtService jwtService;
  private final UserRepository userRepository;

  @Override
  public AuthResponse login(LoginRequest request) {
    authManager.authenticate(
      new UsernamePasswordAuthenticationToken(request.email(), request.password())
    );

    var user = userRepository.findByEmail(request.email())
      .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

    var userDetails = new org.springframework.security.core.userdetails.User(
      user.getEmail(), user.getPassword(),
      java.util.List.of(new org.springframework.security.core.authority.SimpleGrantedAuthority(
        "ROLE_" + user.getRole().name()))
    );

    var token = jwtService.generateToken(userDetails);
    return AuthResponse.of(token, UserResponse.from(user));
  }
}
