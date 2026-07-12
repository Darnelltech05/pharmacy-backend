package com.samedconnect.pharmacy_backend.repository;

import com.samedconnect.pharmacy_backend.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicineRepository extends JpaRepository<Medicine, Long> {

    List<Medicine> findByNameContainingIgnoreCase(String name);

    List<Medicine> findByCategoryContainingIgnoreCase(String category);

    List<Medicine> findByNameContainingIgnoreCaseAndCategoryContainingIgnoreCase(String name, String category);
}
