package com.femi.tickerdesk.repository;

import com.femi.tickerdesk.enumFolder.Department;
import com.femi.tickerdesk.enumFolder.Role;
import com.femi.tickerdesk.enumFolder.Site;
import com.femi.tickerdesk.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByEmployeeId(String employeeId);

    List<User> findByDepartment(Department department);

    List<User> findBySite(Site site);

    List<User> findByRole(Role role);

    List<User> findByDepartmentAndRole(Department department, Role role);

    List<User> findByIsActiveTrue();

    List<User> findByIsActiveFalse();

    @Query("SELECT u FROM User u WHERE u.department = :department AND u.role = :role AND u.isActive = true")
    List<User> findActiveTechniciansByDepartment(@Param("department") Department department, @Param("role") Role role);

    @Query("SELECT u FROM User u WHERE u.firstName LIKE %:name% OR u.lastName LIKE %:name% OR u.email LIKE %:name%")
    Page<User> searchByName(@Param("name") String name, Pageable pageable);

    @Query("SELECT COUNT(u) FROM User u WHERE u.department = :department AND u.isActive = true")
    Long countActiveUsersByDepartment(@Param("department") Department department);

    boolean existsByEmail(String email);

    boolean existsByEmployeeId(String employeeId);

    @Query("SELECT u FROM User u WHERE u.lastLogin < :date")
    List<User> findInactiveUsersSince(@Param("date") LocalDateTime date);
}
