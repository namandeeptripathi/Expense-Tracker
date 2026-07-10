package com.namandeep.expensetracker.repository;

import com.namandeep.expensetracker.entity.Category;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/** Database access for user-owned expense categories. */
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByIdAndUserId(Long id, Long userId);

    Optional<Category> findByUserIdAndNameIgnoreCase(Long userId, String name);

    boolean existsByUserIdAndNameIgnoreCase(Long userId, String name);

    boolean existsByUserIdAndNameIgnoreCaseAndIdNot(Long userId, String name, Long id);

    Page<Category> findByUserId(Long userId, Pageable pageable);

    Page<Category> findByUserIdAndNameContainingIgnoreCase(Long userId, String name, Pageable pageable);
}
