package org.example.inventorysysteamvo1.service;
import org.example.inventorysysteamvo1.service.dto.SupplierRequestDTO;
import org.example.inventorysysteamvo1.service.dto.response.SupplierResponseDTO;

import java.util.List;

public interface SupplierService {

    // ── Create & Update ───────────────────────────────────────────────────

    SupplierResponseDTO createSupplier(SupplierRequestDTO requestDTO);

    SupplierResponseDTO updateSupplier(Long supplierId, SupplierRequestDTO requestDTO);

    // ── Read ──────────────────────────────────────────────────────────────

    SupplierResponseDTO getSupplierById(Long supplierId);

    SupplierResponseDTO getSupplierByName(String name);

    List<SupplierResponseDTO> getAllSuppliers();

    // ── Delete ────────────────────────────────────────────────────────────

    void deleteSupplier(Long supplierId);
}