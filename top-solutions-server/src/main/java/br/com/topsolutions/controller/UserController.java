package br.com.topsolutions.controller;

import br.com.topsolutions.dto.request.CreateUserRequest;
import br.com.topsolutions.dto.request.UpdateUserRequest;
import br.com.topsolutions.dto.response.PageResponse;
import br.com.topsolutions.dto.response.UserResponse;
import br.com.topsolutions.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @GetMapping
  public ResponseEntity<PageResponse<UserResponse>> list(
    @RequestParam(required = false) String search,
    @RequestParam(required = false) Boolean active,
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "10") int size,
    @RequestParam(defaultValue = "name") String sort
  ) {
    return ResponseEntity.ok(userService.findAll(search, active, page, size, sort));
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserResponse> findById(@PathVariable String id) {
    return ResponseEntity.ok(userService.findById(id));
  }

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<UserResponse> create(@Valid @RequestBody CreateUserRequest request) {
    var user = userService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(user);
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<UserResponse> update(
    @PathVariable String id,
    @Valid @RequestBody UpdateUserRequest request
  ) {
    return ResponseEntity.ok(userService.update(id, request));
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> delete(@PathVariable String id) {
    userService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
