package com.samedconnect.pharmacy_backend.controller;

import com.samedconnect.pharmacy_backend.dto.request.MedicineRequest;
import com.samedconnect.pharmacy_backend.dto.request.StockAdjustmentRequest;
import com.samedconnect.pharmacy_backend.dto.request.StockUpdateRequest;
import com.samedconnect.pharmacy_backend.dto.response.MedicineResponse;
import com.samedconnect.pharmacy_backend.entity.Medicine;
import com.samedconnect.pharmacy_backend.service.MedicineService;
import com.samedconnect.pharmacy_backend.utils.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/medicines")
@RequiredArgsConstructor
public class MedicineController {

    private final MedicineService medicineService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACIST')")
    public ResponseEntity<Response<MedicineResponse>> createMedicine(@Valid @RequestBody MedicineRequest request) {
        MedicineResponse response = medicineService.createMedicine(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(Response.success("Medicine created successfully", response));
    }
    @GetMapping("/low-stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACIST')")
    public ResponseEntity<Response<List<MedicineResponse>>> getLowStockMedicines(
            @RequestParam(defaultValue = "10") int threshold) {
        List<MedicineResponse> list = medicineService.getLowStockMedicines(threshold);
        return ResponseEntity.ok(Response.success("Low stock medicines retrieved successfully", list));
    }

    @GetMapping
    public ResponseEntity<Response<List<MedicineResponse>>> getAllMedicines() {
        return ResponseEntity.ok(Response.success("Medicines retrieved successfully", medicineService.getAllMedicines()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<MedicineResponse>> getMedicineById(@PathVariable Long id) {
        return ResponseEntity.ok(Response.success("Medicine retrieved successfully", medicineService.getMedicineById(id)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACIST')")
    public ResponseEntity<Response<MedicineResponse>> updateMedicine(
            @PathVariable Long id,
            @Valid @RequestBody MedicineRequest request
    ) {
        return ResponseEntity.ok(Response.success("Medicine updated successfully", medicineService.updateMedicine(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response<Void>> deleteMedicine(@PathVariable Long id) {
        medicineService.deleteMedicine(id);
        return ResponseEntity.ok(Response.success("Medicine deleted successfully", null));
    }

    @GetMapping("/search")
    public ResponseEntity<Response<List<MedicineResponse>>> searchMedicines(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category
    ) {
        return ResponseEntity.ok(Response.success("Medicines searched successfully", medicineService.searchMedicines(name, category)));
    }

    @PatchMapping("/{id}/stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACIST')")
    public ResponseEntity<Response<MedicineResponse>> updateStockQuantity(
            @PathVariable Long id,
            @Valid @RequestBody StockUpdateRequest request
    ) {
        return ResponseEntity.ok(Response.success("Stock quantity updated successfully", medicineService.updateStockQuantity(id, request.stockQuantity())));
    }

    @PatchMapping("/{id}/stock/increase")
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACIST')")
    public ResponseEntity<Response<MedicineResponse>> increaseStock(
            @PathVariable Long id,
            @Valid @RequestBody StockAdjustmentRequest request
    ) {
        return ResponseEntity.ok(Response.success("Stock increased successfully", medicineService.increaseStock(id, request.quantity())));
    }

    @PatchMapping("/{id}/stock/decrease")
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACIST')")
    public ResponseEntity<Response<MedicineResponse>> decreaseStock(
            @PathVariable Long id,
            @Valid @RequestBody StockAdjustmentRequest request
    ) {
        return ResponseEntity.ok(Response.success("Stock decreased successfully", medicineService.decreaseStock(id, request.quantity())));
    }
}
