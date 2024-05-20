package com.emp.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.emp.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    Optional<User> findByUserId(Long userId); 
    Optional<User> findByUserIdAndRole(Long userId, User.Role role); 
    List<User> findByRole(User.Role role);
}
