package com.samedconnect.pharmacy_backend.service;

import com.samedconnect.pharmacy_backend.dto.request.CreateMedicineRequest;
import com.samedconnect.pharmacy_backend.dto.response.MedicineResponse;
import com.samedconnect.pharmacy_backend.entity.Medicine;
import com.samedconnect.pharmacy_backend.exception.ResourceNotFoundException;
import com.samedconnect.pharmacy_backend.repository.MedicineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicineService {

    private final MedicineRepository medicineRepository;

    public MedicineResponse createMedicine(CreateMedicineRequest request) {

        Medicine medicine = new Medicine();

        medicine.setName(request.getName());
        medicine.setDescription(request.getDescription());
        medicine.setCategory(request.getCategory());
        medicine.setPrice(request.getPrice());
        medicine.setStockQuantity(request.getStockQuantity());
        medicine.setRequiresPrescription(request.getRequiresPrescription());
        medicine.setAvailable(true);

        Medicine savedMedicine = medicineRepository.save(medicine);

        return mapToResponse(savedMedicine);
    }

    public List<MedicineResponse> getAllMedicines() {

        return medicineRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public MedicineResponse getMedicineById(Long id) {

        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Medicine not found"));

        return mapToResponse(medicine);
    }

    public MedicineResponse updateMedicine(Long id, CreateMedicineRequest request) {

        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Medicine not found"));

        medicine.setName(request.getName());
        medicine.setDescription(request.getDescription());
        medicine.setCategory(request.getCategory());
        medicine.setPrice(request.getPrice());
        medicine.setStockQuantity(request.getStockQuantity());
        medicine.setRequiresPrescription(request.getRequiresPrescription());

        Medicine updatedMedicine = medicineRepository.save(medicine);

        return mapToResponse(updatedMedicine);
    }

    public void deleteMedicine(Long id) {

        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Medicine not found"));

        medicineRepository.delete(medicine);
    }

    private MedicineResponse mapToResponse(Medicine medicine) {

        return MedicineResponse.builder()
                .id(medicine.getId())
                .name(medicine.getName())
                .description(medicine.getDescription())
                .price(medicine.getPrice())
                .stockQuantity(medicine.getStockQuantity())
                .build();
    }
}