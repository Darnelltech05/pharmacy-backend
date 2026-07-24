package com.samedconnect.pharmacy_backend.service;

import com.samedconnect.pharmacy_backend.dto.request.MedicineRequest;
import com.samedconnect.pharmacy_backend.dto.response.MedicineResponse;
import com.samedconnect.pharmacy_backend.entity.Medicine;
import com.samedconnect.pharmacy_backend.exception.InvalidStockOperationException;
import com.samedconnect.pharmacy_backend.exception.MedicineNotFoundException;
import com.samedconnect.pharmacy_backend.repository.MedicineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicineService {

    private final MedicineRepository medicineRepository;

    @Transactional
    public MedicineResponse createMedicine(MedicineRequest request) {
        Medicine medicine = Medicine.builder()
                .name(request.name().trim())
                .category(request.category().trim())
                .description(request.description())
                .price(request.price())
                .stockQuantity(request.stockQuantity())
                .isArchived(request.isArchived() != null ? request.isArchived() : false)
                .requiresPrescription(request.requiresPrescription() != null ? request.requiresPrescription() : false)
                .expiryDate(request.expiryDate())
                .build();

        return toResponse(medicineRepository.save(medicine));
    }

    @Transactional(readOnly = true)
    public List<MedicineResponse> getAllMedicines() {
        return medicineRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public MedicineResponse getMedicineById(Long id) {
        return toResponse(findMedicine(id));
    }

    @Transactional
    public MedicineResponse updateMedicine(Long id, MedicineRequest request) {
        Medicine medicine = findMedicine(id);
        medicine.setName(request.name().trim());
        medicine.setCategory(request.category().trim());
        medicine.setDescription(request.description());
        medicine.setPrice(request.price());
        medicine.setStockQuantity(request.stockQuantity());
        if (request.isArchived() != null) medicine.setIsArchived(request.isArchived());
        if (request.requiresPrescription() != null) medicine.setRequiresPrescription(request.requiresPrescription());
        medicine.setExpiryDate(request.expiryDate());

        return toResponse(medicineRepository.save(medicine));
    }

    @Transactional
    public void deleteMedicine(Long id) {
        Medicine medicine = findMedicine(id);
        medicine.setIsArchived(true);
        medicineRepository.save(medicine);
    }

    @Transactional(readOnly = true)
    public List<MedicineResponse> searchMedicines(String name, String category) {
        String medicineName = normalize(name);
        String medicineCategory = normalize(category);

        List<Medicine> medicines;
        if (medicineName != null && medicineCategory != null) {
            medicines = medicineRepository.findByNameContainingIgnoreCaseAndCategoryContainingIgnoreCase(
                    medicineName,
                    medicineCategory
            );
        } else if (medicineName != null) {
            medicines = medicineRepository.findByNameContainingIgnoreCase(medicineName);
        } else if (medicineCategory != null) {
            medicines = medicineRepository.findByCategoryContainingIgnoreCase(medicineCategory);
        } else {
            medicines = medicineRepository.findAll();
        }

        return medicines.stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public MedicineResponse updateStockQuantity(Long id, Integer stockQuantity) {
        Medicine medicine = findMedicine(id);
        medicine.setStockQuantity(stockQuantity);
        return toResponse(medicineRepository.save(medicine));
    }

    @Transactional
    public MedicineResponse increaseStock(Long id, Integer quantity) {
        Medicine medicine = findMedicine(id);
        medicine.setStockQuantity(medicine.getStockQuantity() + quantity);
        return toResponse(medicineRepository.save(medicine));
    }

    @Transactional
    public MedicineResponse decreaseStock(Long id, Integer quantity) {
        Medicine medicine = findMedicine(id);
        if (medicine.getStockQuantity() < quantity) {
            throw new InvalidStockOperationException("Cannot reduce stock below zero. Available stock: "
                    + medicine.getStockQuantity());
        }

        medicine.setStockQuantity(medicine.getStockQuantity() - quantity);
        return toResponse(medicineRepository.save(medicine));
    }

    @Transactional(readOnly = true)
    public List<MedicineResponse> getLowStockMedicines(int threshold) {
        return medicineRepository.findByStockQuantityLessThanEqual(threshold)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private Medicine findMedicine(Long id) {
        return medicineRepository.findById(id)
                .orElseThrow(() -> new MedicineNotFoundException(id));
    }

    private String normalize(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }

    private MedicineResponse toResponse(Medicine medicine) {
        return new MedicineResponse(
                medicine.getId(),
                medicine.getName(),
                medicine.getCategory(),
                medicine.getDescription(),
                medicine.getPrice(),
                medicine.getStockQuantity(),
                medicine.getIsArchived(),
                medicine.getRequiresPrescription(),
                medicine.getExpiryDate(),
                medicine.getCreatedAt(),
                medicine.getUpdatedAt()
        );
    }
}
