package org.example.inventorysysteamvo1.service.serviceIMPL;
import org.example.inventorysysteamvo1.domain.Customer;
import org.example.inventorysysteamvo1.domain.Repo.CustomerRepository;
import org.example.inventorysysteamvo1.domain.Repo.StaffRepository;
import org.example.inventorysysteamvo1.service.CustomerService;
import org.example.inventorysysteamvo1.service.dto.CustomerRequestDTO;
import org.example.inventorysysteamvo1.service.dto.response.CustomerResponseDTO;
import org.example.inventorysysteamvo1.service.mapper.CustomerDtoMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final StaffRepository staffRepository; // Needed to validate the Staff ID
    private final CustomerDtoMapper dtoMapper;

    public CustomerServiceImpl(CustomerRepository customerRepository,
                               StaffRepository staffRepository,
                               CustomerDtoMapper dtoMapper) {
        this.customerRepository = customerRepository;
        this.staffRepository = staffRepository;
        this.dtoMapper = dtoMapper;
    }

    @Override
    public CustomerResponseDTO createCustomer(CustomerRequestDTO requestDTO) {
        // 1. Business Logic Rule: Phone numbers must be unique
        if (customerRepository.existsByPhone(requestDTO.getPhone())) {
            throw new IllegalArgumentException("Customer with phone '" + requestDTO.getPhone() + "' already exists.");
        }

        // 2. Business Logic Rule: Validate that the assigned staff actually exists
        if (requestDTO.getStaffId() != null && !staffRepository.existsById(requestDTO.getStaffId())) {
            throw new IllegalArgumentException("Staff member with ID " + requestDTO.getStaffId() + " does not exist.");
        }

        // 3. Map DTO -> Domain Model
        Customer customer = dtoMapper.toDomain(requestDTO);

        // 4. Save via Domain Repository
        Customer savedCustomer = customerRepository.save(customer);

        // 5. Map Domain Model -> Response DTO
        return dtoMapper.toResponse(savedCustomer);
    }

    @Override
    public CustomerResponseDTO updateCustomer(Long customerId, CustomerRequestDTO requestDTO) {
        // 1. Fetch the existing domain object
        Customer existingCustomer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer with ID " + customerId + " not found."));

        // 2. Business Logic Rule: If they are changing the phone, ensure the new phone isn't taken
        if (!existingCustomer.getPhone().equals(requestDTO.getPhone())
                && customerRepository.existsByPhone(requestDTO.getPhone())) {
            throw new IllegalArgumentException("Customer with phone '" + requestDTO.getPhone() + "' already exists.");
        }

        // 3. Business Logic Rule: Validate that the assigned staff actually exists
        if (requestDTO.getStaffId() != null && !staffRepository.existsById(requestDTO.getStaffId())) {
            throw new IllegalArgumentException("Staff member with ID " + requestDTO.getStaffId() + " does not exist.");
        }

        // 4. Update the existing domain object (Ensure you add these update methods to your Customer domain class!)
        existingCustomer.updateContactDetails(requestDTO.getPhone(), requestDTO.getAddress(), requestDTO.getEmail());
        existingCustomer.updateName(requestDTO.getFirstName(), requestDTO.getLastName());
        existingCustomer.assignStaff(requestDTO.getStaffId());

        // 5. Save and return mapped response
        Customer updatedCustomer = customerRepository.save(existingCustomer);
        return dtoMapper.toResponse(updatedCustomer);
    }

    @Override
    public CustomerResponseDTO getCustomerById(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer with ID " + customerId + " not found."));
        return dtoMapper.toResponse(customer);
    }

    @Override
    public CustomerResponseDTO getCustomerByPhone(String phone) {
        Customer customer = customerRepository.findByPhone(phone)
                .orElseThrow(() -> new IllegalArgumentException("Customer with phone " + phone + " not found."));
        return dtoMapper.toResponse(customer);
    }

    @Override
    public CustomerResponseDTO getCustomerByEmail(String email) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Customer with email " + email + " not found."));
        return dtoMapper.toResponse(customer);
    }

    @Override
    public List<CustomerResponseDTO> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(dtoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CustomerResponseDTO> getCustomersByStaffId(Long staffId) {
        return customerRepository.findByStaffId(staffId)
                .stream()
                .map(dtoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCustomer(Long customerId) {
        customerRepository.deleteById(customerId);
    }
}