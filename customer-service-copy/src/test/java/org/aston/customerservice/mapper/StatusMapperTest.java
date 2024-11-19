package org.aston.customerservice.mapper;

import org.aston.customerservice.dto.response.StatusTypeResponseDto;
import org.aston.customerservice.persistent.entity.Customer;
import org.aston.customerservice.service.AbstractServiceUnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Тест для StatusMapper")
public class StatusMapperTest extends AbstractServiceUnitTest {

    private StatusMapper statusMapper;

    @BeforeEach
    public void setUp() {
        statusMapper = new StatusMapperImpl();
    }

    @Test
    @DisplayName("Тест для statusTypeToDto")
    public void givenCustomerStatus_whenMapToDto_thenSuccessStatusTypeResponse() {
        UUID customerId = UUID.randomUUID();
        Customer customer = createMockCustomer(customerId);

        StatusTypeResponseDto response = new StatusTypeResponseDto((short) 1);
        StatusTypeResponseDto result = statusMapper.statusTypeToDto(customer.getStatus());

        assertEquals(response.getStatusType(), result.getStatusType());
    }
}
