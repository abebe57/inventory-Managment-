package org.example.inventorysysteamvo1.service.serviceIMPL;
import org.example.inventorysysteamvo1.domain.Supplier;
import org.example.inventorysysteamvo1.domain.Repo.SupplierRepository;
import org.example.inventorysysteamvo1.service.SupplierService;
import org.example.inventorysysteamvo1.service.dto.SupplierRequestDTO;
import org.example.inventorysysteamvo1.service.dto.response.SupplierResponseDTO;
import org.example.inventorysysteamvo1.service.mapper.SupplierDtoMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final SupplierDtoMapper dtoMapper;

    public SupplierServiceImpl(SupplierRepository supplierRepository, SupplierDtoMapper dtoMapper) {
        this.supplierRepository = supplierRepository;
        this.dtoMapper = dtoMapper;
    }

    @Override
    public SupplierResponseDTO createSupplier(SupplierRequestDTO requestDTO) {
        // 1. Validation: Supplier names must be unique to prevent duplicates
        if (supplierRepository.existsByName(requestDTO.getName())) {
            throw new IllegalArgumentException("Supplier with name '" + requestDTO.getName() + "' already exists.");
        }

        // 2. Map & Save
        Supplier supplier = dtoMapper.toDomain(requestDTO);
        Supplier savedSupplier = supplierRepository.save(supplier);
        return dtoMapper.toResponse(savedSupplier);
    }

    @Override
    public SupplierResponseDTO updateSupplier(Long supplierId, SupplierRequestDTO requestDTO) {
        Supplier existingSupplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new IllegalArgumentException("Supplier with ID " + supplierId + " not found."));

        // Validation: If changing name, ensure new name is unique
        if (!existingSupplier.getName().equalsIgnoreCase(requestDTO.getName())
                && supplierRepository.existsByName(requestDTO.getName())) {
            throw new IllegalArgumentException("Supplier with name '" + requestDTO.getName() + "' already exists.");
        }

        existingSupplier.updateName(requestDTO.getName());

        existingSupplier.updateContactDetails(
                requestDTO.getPhone(),
                requestDTO.getEmail(),
                requestDTO.getAddress()
        );

        existingSupplier.updateFax(requestDTO.getFax());

        existingSupplier.updateOtherDetails(requestDTO.getOtherDetails());

        Supplier updatedSupplier = supplierRepository.save(existingSupplier);
        return dtoMapper.toResponse(updatedSupplier);
    }

    @Override
    public SupplierResponseDTO getSupplierById(Long supplierId) {
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new IllegalArgumentException("Supplier with ID " + supplierId + " not found."));
        return dtoMapper.toResponse(supplier);
    }

    @Override
    public SupplierResponseDTO getSupplierByName(String name) {
        Supplier supplier = supplierRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Supplier with name " + name + " not found."));
        return dtoMapper.toResponse(supplier);
    }

    @Override
    public List<SupplierResponseDTO> getAllSuppliers() {
        return supplierRepository.findAll()
                .stream()
                .map(dtoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteSupplier(Long supplierId) {
        supplierRepository.deleteById(supplierId);
    }
}