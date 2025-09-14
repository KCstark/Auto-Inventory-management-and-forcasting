package com.inventory_forcasting.controllers;

import com.inventory_forcasting.models.EventType;
import com.inventory_forcasting.models.UsersDTO;
import com.inventory_forcasting.service.AuditService;
import com.inventory_forcasting.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/data")
public class UserController {

    private final UserService userService;
    private final AuditService auditService;

    //health check
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        auditService.logEvent(EventType.HEALTH_CHECK, "GET /data/health","Health check successful");
        return ResponseEntity.ok("OK, Running...");
    }

    //register user
    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@RequestBody UsersDTO user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.register(user));
    }

    //login user
    @PostMapping("/login")
    public ResponseEntity<Object> loginUser(@RequestBody UsersDTO user) {
        return ResponseEntity.ok(userService.verify(user));
    }

    //logout user
    @PostMapping("/logout")
    public ResponseEntity<Object> logoutUser(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatusCode.valueOf(204)).body(userService.logoutUser(request));
    }

    //get all users
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<Object> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }


}
