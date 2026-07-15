package com.samedconnect.pharmacy_backend.controller;

import com.samedconnect.pharmacy_backend.dto.request.CreateMedicineRequest;
import com.samedconnect.pharmacy_backend.dto.response.MedicineResponse;
import com.samedconnect.pharmacy_backend.service.MedicineService;
import com.samedconnect.pharmacy_backend.utils.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medicines")
@RequiredArgsConstructor
public class MedicineController {

    private final MedicineService medicineService;

    @PostMapping
    public ResponseEntity<Response<MedicineResponse>> createMedicine(
            @Valid @RequestBody CreateMedicineRequest request) {

        MedicineResponse response = medicineService.createMedicine(request);

        return ResponseEntity.ok(
                Response.success("Medicine created successfully", response)
        );
    }

    @GetMapping
    public ResponseEntity<Response<List<MedicineResponse>>> getAllMedicines() {

        return ResponseEntity.ok(
                Response.success(
                        "Medicines retrieved successfully",
                        medicineService.getAllMedicines()
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<MedicineResponse>> getMedicineById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                Response.success(
                        "Medicine retrieved successfully",
                        medicineService.getMedicineById(id)
                )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<MedicineResponse>> updateMedicine(
            @PathVariable Long id,
            @Valid @RequestBody CreateMedicineRequest request) {

        MedicineResponse response =
                medicineService.updateMedicine(id, request);

        return ResponseEntity.ok(
                Response.success("Medicine updated successfully", response)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<String>> deleteMedicine(
            @PathVariable Long id) {

        medicineService.deleteMedicine(id);

        return ResponseEntity.ok(
                Response.success("Medicine deleted successfully", "Deleted")
        );
    }
}