package com.example.IotProject.controller;

import com.example.IotProject.model.UserModel;
// import com.example.IotProject.service.AdminService;
import com.example.IotProject.service.UserService.IUserService;
// import com.example.IotProject.service.UserService.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/admin")
public class AdminController {

    private final IUserService userService;

    public AdminController(IUserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ROLE_Admin')")
    @PostMapping("/users")
    public ResponseEntity<UserModel> addUser(@RequestBody UserModel user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addUser(user));
    }

    @PreAuthorize("hasRole('ROLE_Admin')")
    @GetMapping("/users")
    public ResponseEntity<List<UserModel>> getAllUser() {
        return ResponseEntity.ok(userService.getAllUser());
    }

    @PreAuthorize("hasRole('ROLE_Admin')")
    @GetMapping("/users/role/{role}")
    public ResponseEntity<List<UserModel>> getUserByRole(@PathVariable String role) {
        return ResponseEntity.ok(userService.getUserByRole(role));
    }

    @PreAuthorize("hasRole('ROLE_Admin')")
    @GetMapping("/users/{id}")
    public ResponseEntity<UserModel> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PreAuthorize("hasRole('ROLE_Admin')")
    @PutMapping("/users/{id}")
    public ResponseEntity<UserModel> updateUser(@PathVariable Long id, @RequestBody UserModel user) {
//        user.setId(id);
        return ResponseEntity.ok(userService.updateUser(user));
    }

    @PreAuthorize("hasRole('Admin')")
    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully!");
    }
}
