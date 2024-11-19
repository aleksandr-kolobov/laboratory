package org.aston.customerservice.mapper;

import org.aston.customerservice.dto.response.CustomerFullNameResponseDto;
import org.aston.customerservice.dto.response.CustomerIdResponseDto;
import org.aston.customerservice.dto.response.CustomerResponseDto;
import org.aston.customerservice.persistent.entity.Customer;
import org.aston.customerservice.service.AbstractServiceUnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Тест для CustomerMapper")
public class CustomerMapperTest extends AbstractServiceUnitTest {

    private CustomerMapper customerMapper;

    @BeforeEach
    public void setUp() {
        customerMapper = new CustomerMapperImpl();
    }

    @Test
    @DisplayName("Тест для toCustomerResponseDto")
    public void givenCustomer_whenToCustomerResponseDto_thenCustomerResponseDto() {

        UUID customerId = UUID.randomUUID();
        Customer customer = createMockCustomer(customerId);
        CustomerResponseDto customerResponseDto = createMockCustomerResponse();

        CustomerResponseDto result = customerMapper.toCustomerResponseDto(customer);

        assertEquals(customerResponseDto, result);
    }

    @Test
    @DisplayName("Тест для toCustomerIdDto")
    public void givenCustomer_whenToCustomerIdDto_thenIdDto() {

        UUID customerId = UUID.randomUUID();
        Customer customer = createMockCustomer(customerId);
        CustomerIdResponseDto customerIdResponseDto = new CustomerIdResponseDto();
        customerIdResponseDto.setCustomerId(customerId.toString());

        CustomerIdResponseDto result = customerMapper.toCustomerIdDto(customer);

        assertEquals(customerIdResponseDto, result);
    }

    @Test
    @DisplayName("Тест для CustomerFullNameResponseDto")
    public void givenCustomer_whenToCustomerFullNameResponseDto_thenCustomerFullNameResponseDto() {

        UUID customerId = UUID.randomUUID();
        Customer customer = createMockCustomer(customerId);
        CustomerFullNameResponseDto result = customerMapper.toCustomerFullNameResponseDto(customer);

        assertAll("customerFullNameResponseDto",
                () -> assertEquals(customer.getFirstName(), result.getFirstName()),
                () -> assertEquals(customer.getLastName(), result.getLastName()),
                () -> assertEquals(customer.getMiddleName(), result.getMiddleName())
        );
    }
}
