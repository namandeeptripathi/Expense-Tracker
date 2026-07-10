package com.namandeep.expensetracker.repository;

import com.namandeep.expensetracker.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/** Database access for application users. */
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
