package org.aston.customerservice.service.impl;

import org.aston.customerservice.dto.request.PhoneNumberRequestDto;
import org.aston.customerservice.dto.response.StatusResponseDto;
import org.aston.customerservice.dto.response.StatusTypeResponseDto;
import org.aston.customerservice.exception.CustomerException;
import org.aston.customerservice.mapper.StatusMapper;
import org.aston.customerservice.persistent.entity.Country;
import org.aston.customerservice.persistent.entity.Customer;
import org.aston.customerservice.persistent.entity.FamilyStatus;
import org.aston.customerservice.persistent.entity.Status;
import org.aston.customerservice.persistent.repository.CustomerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тест для StatusServiceImpl")
class StatusServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private StatusMapper statusMapper;
    @InjectMocks
    private StatusServiceImpl statusService;

    @Test
    void testGetCustomerStatus_whenGetCustomerStatus_thenStatusReturned() {
        PhoneNumberRequestDto phoneNumberRequestDto = new PhoneNumberRequestDto("12345678900");
        Status expectedStatus = new Status((short) 1, "ok");
        Customer mockCustomer = createMockCustomer();
        mockCustomer.setStatus(expectedStatus);
        when(customerRepository.findByPhoneNumber(phoneNumberRequestDto.getPhoneNumber()))
                .thenReturn(Optional.of(mockCustomer));

        StatusResponseDto actualStatus = statusService.getCustomerStatus(phoneNumberRequestDto);

        assertEquals(expectedStatus.getStatusType(), actualStatus.getStatusType());
        assertEquals("Без ограничений", actualStatus.getMessage());
        verify(customerRepository, times(1)).findByPhoneNumber(phoneNumberRequestDto.getPhoneNumber());
    }

    @Test
    void testGetCustomerStatus_whenGetCustomerStatus_thenThrowNotFoundException() {
        PhoneNumberRequestDto phoneNumberRequestDto = new PhoneNumberRequestDto("nonExistentNumber");
        when(customerRepository.findByPhoneNumber(phoneNumberRequestDto.getPhoneNumber())).thenReturn(Optional.empty());

        assertThrows(CustomerException.class, () -> statusService.getCustomerStatus(phoneNumberRequestDto));
        verify(customerRepository, times(1)).findByPhoneNumber(phoneNumberRequestDto.getPhoneNumber());
    }

    @Test
    void testGetCustomerStatus_whenGetCustomerStatus_thenThrowForbiddenException() {
        PhoneNumberRequestDto phoneNumberRequestDto = new PhoneNumberRequestDto("12345678900");
        Customer mockCustomer = createMockCustomer();
        mockCustomer.getStatus().setStatusType((short) 3);
        when(customerRepository.findByPhoneNumber(phoneNumberRequestDto.getPhoneNumber())).thenReturn(Optional.of(mockCustomer));

        assertThrows(CustomerException.class, () -> statusService.getCustomerStatus(phoneNumberRequestDto));
        verify(customerRepository, times(1)).findByPhoneNumber(phoneNumberRequestDto.getPhoneNumber());
    }

    @Test
    void givenStatusType_whenGetCustomerStatusById_thenStatusTypeReturn() {
        StatusTypeResponseDto response = new StatusTypeResponseDto();
        response.setStatusType((short) 1);
        Status expectedStatus = new Status((short) 1, "ok");
        Customer mockCustomer = createMockCustomer();
        mockCustomer.setStatus(expectedStatus);

        when(customerRepository.findById(mockCustomer.getCustomerId())).thenReturn(Optional.of(mockCustomer));
        when(statusMapper.statusTypeToDto(mockCustomer.getStatus())).thenReturn(response);

        StatusTypeResponseDto actualStatus = statusService.getStatusType(String.valueOf(mockCustomer.getCustomerId()));

        assertEquals(response.getStatusType(), actualStatus.getStatusType());
        verify(customerRepository, times(1)).findById(mockCustomer.getCustomerId());
    }

    private Customer createMockCustomer() {
        return new Customer(
                UUID.randomUUID(),                    // customerId
                null,                                 // userProfile (можно передать null, в тесте не используется)
                new HashSet<>(),                      // addresses (можно передать пустой Set, в тесте не используется)
                new ArrayList<>(),                    // documents (можно передать пустой List, в тесте не используется)
                "Иван",                               // firstName
                "Иванов",                             // lastName
                "Иванович",                           // middleName
                "+12345678900",                       // phoneNumber
                "ваня@mai.ru",                        // email
                'M',                                  // gender
                LocalDate.of(1990, 1, 1),             // birthDate
                new FamilyStatus(),                   // familyStatus
                (short) 0,                            // childCount
                LocalDate.now(),                      // registrationDateBank
                "Вопросы?",                           // secretQuestion
                "Ответы!",                            // secretAnswer
                new Country(),                        // country
                new Status(),                         // status
                true                                  // smsNotification
        );
    }
}