package com.femi.tickerdesk.service;

import com.femi.tickerdesk.auth.JwtService;
import com.femi.tickerdesk.enumFolder.Department;
import com.femi.tickerdesk.enumFolder.Role;
import com.femi.tickerdesk.enumFolder.Site;
import com.femi.tickerdesk.model.User;
import com.femi.tickerdesk.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    public User createUser(User user) {
        log.info("Creating new user with email: {}", user.getEmail());
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("User with this email already exists");
        }
        if (user.getEmployeeId() != null && userRepository.existsByEmployeeId(user.getEmployeeId())) {
            throw new IllegalArgumentException("User with this employee ID already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (user.getIsActive() == null) {
            user.setIsActive(true);
        }

        if (user.getRole() == null) {
            user.setRole(Role.USER);
        }

        User savedUser = userRepository.save(user);
        log.info("User created successfully with ID: {}", savedUser.getId());
        return savedUser;
    }

    @Override
    public User updateUser(Long id, User user) {
        log.info("Updating user with ID: {}", id);

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));
        if (!existingUser.getEmail().equals(user.getEmail()) &&
                userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("User with this email already exists");
        }
        if (user.getEmployeeId() != null &&
                !user.getEmployeeId().equals(existingUser.getEmployeeId()) &&
                userRepository.existsByEmployeeId(user.getEmployeeId())) {
            throw new IllegalArgumentException("User with this employee ID already exists");
        }
        existingUser.setEmail(user.getEmail());
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setDepartment(user.getDepartment());
        existingUser.setSite(user.getSite());
        existingUser.setRole(user.getRole());
        existingUser.setEmployeeId(user.getEmployeeId());

        User updatedUser = userRepository.save(existingUser);
        log.info("User updated successfully with ID: {}", updatedUser.getId());
        return updatedUser;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        log.debug("Finding user by ID: {}", id);
        return userRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        log.debug("Finding user by email: {}", email);
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmployeeId(String employeeId) {
        log.debug("Finding user by employee ID: {}", employeeId);
        return userRepository.findByEmployeeId(employeeId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        log.debug("Finding all users");
        return userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> findAll(Pageable pageable) {
        log.debug("Finding all users with pagination");
        return userRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findByDepartment(Department department) {
        log.debug("Finding users by department: {}", department);
        return userRepository.findByDepartment(department);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findBySite(Site site) {
        log.debug("Finding users by site: {}", site);
        return userRepository.findBySite(site);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findByRole(Role role) {
        log.debug("Finding users by role: {}", role);
        return userRepository.findByRole(role);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findByDepartmentAndRole(Department department, Role role) {
        log.debug("Finding users by department: {} and role: {}", department, role);
        return userRepository.findByDepartmentAndRole(department, role);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findActiveUsers() {
        log.debug("Finding all active users");
        return userRepository.findByIsActiveTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findInactiveUsers() {
        log.debug("Finding all inactive users");
        return userRepository.findByIsActiveFalse();
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findActiveTechniciansByDepartment(Department department) {
        log.debug("Finding active technicians by department: {}", department);
        return userRepository.findActiveTechniciansByDepartment(department, Role.TECHNICIAN);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> searchByName(String name, Pageable pageable) {
        log.debug("Searching users by name: {}", name);
        return userRepository.searchByName(name, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countActiveUsersByDepartment(Department department) {
        log.debug("Counting active users by department: {}", department);
        return userRepository.countActiveUsersByDepartment(department);
    }

    @Override
    public User activateUser(Long id) {
        log.info("Activating user with ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));

        user.setIsActive(true);
        User activatedUser = userRepository.save(user);
        log.info("User activated successfully with ID: {}", activatedUser.getId());
        return activatedUser;
    }

    @Override
    public User deactivateUser(Long id) {
        log.info("Deactivating user with ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));

        user.setIsActive(false);
        User deactivatedUser = userRepository.save(user);
        log.info("User deactivated successfully with ID: {}", deactivatedUser.getId());
        return deactivatedUser;
    }

    @Override
    public void deleteUser(Long id) {
        log.info("Deleting user with ID: {}", id);

        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found with ID: " + id);
        }

        userRepository.deleteById(id);
        log.info("User deleted successfully with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        log.debug("Checking if user exists by email: {}", email);
        return userRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmployeeId(String employeeId) {
        log.debug("Checking if user exists by employee ID: {}", employeeId);
        return userRepository.existsByEmployeeId(employeeId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findInactiveUsersSince(LocalDateTime date) {
        log.debug("Finding inactive users since: {}", date);
        return userRepository.findInactiveUsersSince(date);
    }

    @Override
    public User updateLastLogin(Long id) {
        log.debug("Updating last login for user ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));

        user.setLastLogin(LocalDateTime.now());
        return userRepository.save(user);
    }

    @Override
    public User changePassword(Long id, String newPassword) {
        log.info("Changing password for user ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));
        user.setPassword(passwordEncoder.encode(newPassword));

        User updatedUser = userRepository.save(user);
        log.info("Password changed successfully for user ID: {}", updatedUser.getId());
        return updatedUser;
    }

    public UserStatistics getUserStatistics() {
        log.debug("Generating user statistics");

        long totalUsers = userRepository.count();
        long activeUsers = userRepository.findByIsActiveTrue().size();
        long inactiveUsers = totalUsers - activeUsers;

        return UserStatistics.builder()
                .totalUsers(totalUsers)
                .activeUsers(activeUsers)
                .inactiveUsers(inactiveUsers)
                .usersByDepartment(getUserCountByDepartment())
                .usersByRole(getUserCountByRole())
                .build();
    }

    private java.util.Map<Department, Long> getUserCountByDepartment() {
        java.util.Map<Department, Long> departmentCount = new java.util.HashMap<>();
        for (Department dept : Department.values()) {
            departmentCount.put(dept, countActiveUsersByDepartment(dept));
        }
        return departmentCount;
    }

    private java.util.Map<Role, Long> getUserCountByRole() {
        java.util.Map<Role, Long> roleCount = new java.util.HashMap<>();
        for (Role role : Role.values()) {
            roleCount.put(role, (long) userRepository.findByRole(role).size());
        }
        return roleCount;
    }

    public List<User> createUsers(List<User> users) {
        log.info("Creating {} users in bulk", users.size());

        for (User user : users) {
            if (userRepository.existsByEmail(user.getEmail())) {
                throw new IllegalArgumentException("User with email " + user.getEmail() + " already exists");
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            if (user.getIsActive() == null) {
                user.setIsActive(true);
            }
            if (user.getRole() == null) {
                user.setRole(Role.USER);
            }
        }

        List<User> savedUsers = userRepository.saveAll(users);
        log.info("Successfully created {} users", savedUsers.size());
        return savedUsers;
    }

    @lombok.Data
    @lombok.Builder
    public static class UserStatistics {
        private long totalUsers;
        private long activeUsers;
        private long inactiveUsers;
        private java.util.Map<Department, Long> usersByDepartment;
        private java.util.Map<Role, Long> usersByRole;
    }
}