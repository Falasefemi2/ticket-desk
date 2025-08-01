package com.femi.tickerdesk.controller;

import com.femi.tickerdesk.dto.*;
import com.femi.tickerdesk.enumFolder.Department;
import com.femi.tickerdesk.enumFolder.Role;
import com.femi.tickerdesk.enumFolder.Site;
import com.femi.tickerdesk.model.User;
import com.femi.tickerdesk.service.UserService;
import com.femi.tickerdesk.service.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")

public class UserController {

    private final UserService userService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse<User>> createUser(@Valid @RequestBody CreateUserRequest request) {
        log.info("Creating new user with email: {}", request.getEmail());

        try {
            User user = User.builder()
                    .email(request.getEmail())
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .password(request.getPassword())
                    .department(request.getDepartment())
                    .site(request.getSite())
                    .role(request.getRole())
                    .employeeId(request.getEmployeeId())
                    .isActive(true)
                    .build();

            User createdUser = userService.createUser(user);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("User created successfully", createdUser));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            log.error("Error creating user: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to create user"));
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or authentication.name == @userService.findById(#id).orElse(new com.femi.tickerdesk.model.User()).email")
    public ResponseEntity<ApiResponse<User>> getUserById(@PathVariable Long id) {
        log.debug("Fetching user with ID: {}", id);

        Optional<User> user = userService.findById(id);

        return user.map(value -> ResponseEntity.ok(ApiResponse.success("User found", value))).orElseGet(() -> ResponseEntity.notFound()
                .build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or authentication.name == @userService.findById(#id).orElse(new com.femi.tickerdesk.model.User()).email")
    public ResponseEntity<ApiResponse<User>> updateUser(@PathVariable Long id,
                                                        @Valid @RequestBody UpdateUserRequest request) {
        log.info("Updating user with ID: {}", id);

        try {
            User user = User.builder()
                    .email(request.getEmail())
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .department(request.getDepartment())
                    .site(request.getSite())
                    .role(request.getRole())
                    .employeeId(request.getEmployeeId())
                    .build();

            User updatedUser = userService.updateUser(id, user);

            return ResponseEntity.ok(ApiResponse.success("User updated successfully", updatedUser));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            log.error("Error updating user: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to update user"));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        log.info("Deleting user with ID: {}", id);

        try {
            userService.deleteUser(id);
            return ResponseEntity.ok(ApiResponse.success("User deleted successfully", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound()
                    .build();
        } catch (Exception e) {
            log.error("Error deleting user: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to delete user"));
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse<Page<User>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "firstName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        log.debug("Fetching all users - page: {}, size: {}", page, size);

        Sort sort = Sort.by(sortDir.equalsIgnoreCase("desc") ?
                Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<User> users = userService.findAll(pageable);

        return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully", users));
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse<Page<User>>> searchUsers(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.debug("Searching users with query: {}", query);

        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = userService.searchByName(query, pageable);

        return ResponseEntity.ok(ApiResponse.success("Search completed", users));
    }

    @GetMapping("/department/{department}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse<List<User>>> getUsersByDepartment(@PathVariable Department department) {
        log.debug("Fetching users by department: {}", department);

        List<User> users = userService.findByDepartment(department);

        return ResponseEntity.ok(ApiResponse.success("Users retrieved by department", users));
    }

    @GetMapping("/site/{site}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse<List<User>>> getUsersBySite(@PathVariable Site site) {
        log.debug("Fetching users by site: {}", site);

        List<User> users = userService.findBySite(site);

        return ResponseEntity.ok(ApiResponse.success("Users retrieved by site", users));
    }

    @GetMapping("/role/{role}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<User>>> getUsersByRole(@PathVariable Role role) {
        log.debug("Fetching users by role: {}", role);

        List<User> users = userService.findByRole(role);

        return ResponseEntity.ok(ApiResponse.success("Users retrieved by role", users));
    }

    @GetMapping("/active")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse<List<User>>> getActiveUsers() {
        log.debug("Fetching active users");

        List<User> users = userService.findActiveUsers();

        return ResponseEntity.ok(ApiResponse.success("Active users retrieved", users));
    }

    @GetMapping("/inactive")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<User>>> getInactiveUsers() {
        log.debug("Fetching inactive users");

        List<User> users = userService.findInactiveUsers();

        return ResponseEntity.ok(ApiResponse.success("Inactive users retrieved", users));
    }

    @PutMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<User>> activateUser(@PathVariable Long id) {
        log.info("Activating user with ID: {}", id);

        try {
            User user = userService.activateUser(id);
            return ResponseEntity.ok(ApiResponse.success("User activated successfully", user));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error activating user: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to activate user"));
        }
    }

    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<User>> deactivateUser(@PathVariable Long id) {
        log.info("Deactivating user with ID: {}", id);

        try {
            User user = userService.deactivateUser(id);
            return ResponseEntity.ok(ApiResponse.success("User deactivated successfully", user));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error deactivating user: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to deactivate user"));
        }
    }

    @PutMapping("/{id}/change-password")
    @PreAuthorize("hasRole('ADMIN') or authentication.name == @userService.findById(#id).orElse(new com.femi.tickerdesk.model.User()).email")
    public ResponseEntity<ApiResponse<Void>> changePassword(@PathVariable Long id,
                                                            @Valid @RequestBody ChangePasswordRequest request) {
        log.info("Changing password for user ID: {}", id);

        try {
            userService.changePassword(id, request.getNewPassword());
            return ResponseEntity.ok(ApiResponse.success("Password changed successfully", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            log.error("Error changing password: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to change password"));
        }
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<User>> getCurrentUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        log.debug("Fetching profile for user: {}", email);

        Optional<User> user = userService.findByEmail(email);

        if (user.isPresent()) {
            return ResponseEntity.ok(ApiResponse.success("Profile retrieved", user.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<User>> updateCurrentUserProfile(@Valid @RequestBody UpdateProfileRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        log.info("Updating profile for user: {}", email);

        try {
            Optional<User> currentUser = userService.findByEmail(email);

            if (currentUser.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            User user = User.builder()
                    .email(request.getEmail())
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .build();

            User updatedUser = userService.updateUser(currentUser.get().getId(), user);

            return ResponseEntity.ok(ApiResponse.success("Profile updated successfully", updatedUser));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            log.error("Error updating profile: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to update profile"));
        }
    }

    @GetMapping("/statistics")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse<UserServiceImpl.UserStatistics>> getUserStatistics() {
        log.debug("Fetching user statistics");

        try {
            UserServiceImpl.UserStatistics stats = ((UserServiceImpl) userService).getUserStatistics();
            return ResponseEntity.ok(ApiResponse.success("Statistics retrieved", stats));
        } catch (Exception e) {
            log.error("Error fetching statistics: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to fetch statistics"));
        }
    }

    @GetMapping("/technicians/{department}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('TECHNICIAN')")
    public ResponseEntity<ApiResponse<List<User>>> getTechniciansByDepartment(@PathVariable Department department) {
        log.debug("Fetching technicians for department: {}", department);

        List<User> technicians = userService.findActiveTechniciansByDepartment(department);

        return ResponseEntity.ok(ApiResponse.success("Technicians retrieved", technicians));
    }

    @PostMapping("/bulk")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<User>>> createBulkUsers(@Valid @RequestBody List<CreateUserRequest> requests) {
        log.info("Creating {} users in bulk", requests.size());

        try {
            List<User> users = requests.stream()
                    .map(request -> User.builder()
                            .email(request.getEmail())
                            .firstName(request.getFirstName())
                            .lastName(request.getLastName())
                            .password(request.getPassword())
                            .department(request.getDepartment())
                            .site(request.getSite())
                            .role(request.getRole())
                            .employeeId(request.getEmployeeId())
                            .isActive(true)
                            .build())
                    .toList();

            List<User> createdUsers = ((UserServiceImpl) userService).createUsers(users);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Users created successfully", createdUsers));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            log.error("Error creating bulk users: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to create users"));
        }
    }

    @GetMapping("/check-email")
    public ResponseEntity<ApiResponse<Boolean>> checkEmailExists(@RequestParam String email) {
        log.debug("Checking if email exists: {}", email);

        boolean exists = userService.existsByEmail(email);

        return ResponseEntity.ok(ApiResponse.success("Email check completed", exists));
    }

    @GetMapping("/check-employee-id")
    public ResponseEntity<ApiResponse<Boolean>> checkEmployeeIdExists(@RequestParam String employeeId) {
        log.debug("Checking if employee ID exists: {}", employeeId);

        boolean exists = userService.existsByEmployeeId(employeeId);

        return ResponseEntity.ok(ApiResponse.success("Employee ID check completed", exists));
    }
}

