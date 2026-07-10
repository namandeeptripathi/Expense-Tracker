package com.namandeep.expensetracker.repository;

import com.namandeep.expensetracker.entity.Category;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/** Database access for reusable expense categories. */
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByNameIgnoreCase(String name);
}
