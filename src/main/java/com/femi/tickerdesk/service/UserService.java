package com.femi.tickerdesk.service;

import com.femi.tickerdesk.enumFolder.Department;
import com.femi.tickerdesk.enumFolder.Role;
import com.femi.tickerdesk.enumFolder.Site;
import com.femi.tickerdesk.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserService {

    User createUser(User user);

    User updateUser(Long id, User user);

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    Optional<User> findByEmployeeId(String employeeId);

    List<User> findAll();

    Page<User> findAll(Pageable pageable);

    List<User> findByDepartment(Department department);

    List<User> findBySite(Site site);

    List<User> findByRole(Role role);

    List<User> findByDepartmentAndRole(Department department, Role role);

    List<User> findActiveUsers();

    List<User> findInactiveUsers();

    List<User> findActiveTechniciansByDepartment(Department department);

    Page<User> searchByName(String name, Pageable pageable);

    Long countActiveUsersByDepartment(Department department);

    User activateUser(Long id);

    User deactivateUser(Long id);

    void deleteUser(Long id);

    boolean existsByEmail(String email);

    boolean existsByEmployeeId(String employeeId);

    List<User> findInactiveUsersSince(LocalDateTime date);

    User updateLastLogin(Long id);

    User changePassword(Long id, String newPassword);
}
