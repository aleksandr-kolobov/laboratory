package org.aston.customerservice.service.impl;

import org.aston.customerservice.dto.response.CustomerIdResponseDto;
import org.aston.customerservice.dto.response.CustomerResponseDto;
import org.aston.customerservice.dto.request.EmailRequestDto;
import org.aston.customerservice.dto.request.PhoneNumberRequestDto;
import org.aston.customerservice.dto.response.CustomerFullNameResponseDto;
import org.aston.customerservice.exception.CustomerException;
import org.aston.customerservice.exception.UserProfileException;
import org.aston.customerservice.mapper.CustomerMapper;
import org.aston.customerservice.persistent.entity.Customer;
import org.aston.customerservice.persistent.repository.CustomerRepository;
import org.aston.customerservice.persistent.repository.UserProfileRepository;
import org.aston.customerservice.service.AbstractServiceUnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import java.util.Optional;
import java.util.UUID;

import static org.aston.customerservice.data.AuthTestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Тест для CustomerService")
public class CustomerServiceImplTest extends AbstractServiceUnitTest {

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private CustomerMapper customerMapper;
    @Mock
    private UserProfileRepository userProfileRepository;
    @InjectMocks
    private CustomerServiceImpl customerService;

    private final UUID customerId = UUID.randomUUID();
    private final Customer customer = createMockCustomer(customerId);


    @Test
    @DisplayName("Тест для findById OK")
    public void givenCustomerId_whenFindById_thenCustomerResponse() {

        CustomerResponseDto customerResponse = createMockCustomerResponse();

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(customerMapper.toCustomerResponseDto(customer)).thenReturn(customerResponse);

        CustomerResponseDto result = customerService.findById(customerId);

        assertEquals(EXCEPTED_PHONE, result.getPhoneNumber());
        assertEquals(EXCEPTED_EMAIL, result.getEmail());
        verify(customerRepository, times(1)).findById(customerId);
    }

    @Test
    @DisplayName("Тест для findById NotFound")
    public void givenCustomerId_whenFindById_thenCustomerNotFound() {

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        assertThrows(CustomerException.class, () -> customerService.findById(customerId));
        verify(customerRepository, times(1)).findById(customerId);
    }

    @Test
    @DisplayName("Тест для findByPhoneNumber OK")
    public void givenPhoneNumber_whenFindByPhoneNumber_thenCustomerResponse() {

        String phoneNumber = customer.getPhoneNumber();
        CustomerResponseDto customerResponse = createMockCustomerResponse();

        when(customerRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.of(customer));
        when(customerMapper.toCustomerResponseDto(customer)).thenReturn(customerResponse);

        CustomerResponseDto result = customerService.findByPhoneNumber(phoneNumber);

        assertEquals(EXCEPTED_PHONE, result.getPhoneNumber());
        assertEquals(EXCEPTED_EMAIL, result.getEmail());
        verify(customerRepository, times(1)).findByPhoneNumber(phoneNumber);
    }

    @Test
    @DisplayName("Тест для findByPhoneNumber NotFound")
    public void givenPhoneNumber_whenFindByPhoneNumber_thenNotFound() {

        String phoneNumber = customer.getPhoneNumber();
        when(customerRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.empty());

        assertThrows(CustomerException.class, () -> customerService.findByPhoneNumber(phoneNumber));
        verify(customerRepository, times(1)).findByPhoneNumber(phoneNumber);
    }

    @Test
    @DisplayName("Тест для проверки отсутствия регистрации л/к OK")
    void givenRegisteredCustomerWithoutExistUserProfile_whenCheckMissRegistrationByPhoneNumber_thenReturnCustomerId() {

        CustomerIdResponseDto expectedCustomerIdResponseDto = new CustomerIdResponseDto();

        when(customerRepository.findByPhoneNumber(EXCEPTED_PHONE)).thenReturn(Optional.of(customer));
        when(userProfileRepository.existsByCustomerCustomerId(customer.getCustomerId())).thenReturn(false);
        when(customerMapper.toCustomerIdDto(customer)).thenReturn(expectedCustomerIdResponseDto);

        CustomerIdResponseDto actualCustomerIdResponseDto = customerService.checkMissRegistration(new PhoneNumberRequestDto(EXCEPTED_PHONE));

        assertEquals(expectedCustomerIdResponseDto, actualCustomerIdResponseDto);
    }

    @Test
    @DisplayName("Тест для проверки отсутствия регистрации л/к уже зарегистрирован")
    void givenRegisteredCustomerWithExistUserProfile_whenCheckMissRegistrationByPhoneNumber_thenThrowUserProfileException() {

        when(customerRepository.findByPhoneNumber(EXCEPTED_PHONE)).thenReturn(Optional.of(customer));
        when(userProfileRepository.existsByCustomerCustomerId(customer.getCustomerId())).thenReturn(true);

        assertThrows(UserProfileException.class, () -> customerService.checkMissRegistration(
                new PhoneNumberRequestDto(EXCEPTED_PHONE)));
    }

    @Test
    @DisplayName("Тест для проверки отсутствия  регистрации л/к NotFound")
    void givenNotRegisteredCustomer_whenCheckMissRegistrationByPhoneNumber_thenThrowCustomerException() {

        when(customerRepository.findByPhoneNumber(EXCEPTED_PHONE)).thenReturn(Optional.empty());

        assertThrows(CustomerException.class, () -> customerService.checkMissRegistration(
                new PhoneNumberRequestDto(EXCEPTED_PHONE)));
    }


    @Test
    @DisplayName("Тест для проверки наличия регистрации л/к уже зарегистрирован")
    void givenRegisteredCustomerWithoutExistUserProfile_whenCheckRegistrationByPhoneNumber_thenThrowUserProfileException() {

        when(customerRepository.findByPhoneNumber(EXCEPTED_PHONE)).thenReturn(Optional.of(customer));
        when(userProfileRepository.existsByCustomerCustomerId(customer.getCustomerId())).thenReturn(false);

        assertThrows(UserProfileException.class, () -> customerService.checkRegistration(
                new PhoneNumberRequestDto(EXCEPTED_PHONE)));
    }

    @Test
    @DisplayName("Тест для проверки наличия  регистрации л/к NotFound")
    void givenNotRegisteredCustomer_whenCheckRegistrationByPhoneNumber_thenThrowCustomerException() {

        when(customerRepository.findByPhoneNumber(EXCEPTED_PHONE)).thenReturn(Optional.empty());

        assertThrows(CustomerException.class, () -> customerService.checkRegistration(
                new PhoneNumberRequestDto(EXCEPTED_PHONE)));
    }

    @Test
    @DisplayName("Тест для обновления email OK")
    void givenNotExists_whenChangeEmail_thenEmailChangedSuccessfully() {

        String phoneNumber = "79234251422";
        String email = "newemail@example.com";

        EmailRequestDto newEmail = new EmailRequestDto();
        newEmail.setEmail(email);

        Customer existingCustomer = new Customer();
        existingCustomer.setPhoneNumber(phoneNumber);
        existingCustomer.setEmail("oldemail@example.com");

        when(customerRepository.findByPhoneNumber(phoneNumber)).thenReturn(java.util.Optional.of(existingCustomer));
        when(customerRepository.existsByEmail(email)).thenReturn(false);

        customerService.updateEmail(newEmail, phoneNumber);

        assertEquals(email, existingCustomer.getEmail());
        verify(customerRepository, times(1)).save(existingCustomer);
    }

    @Test
    @DisplayName("Тест для обновления email NotOK")
    void givenExistingEmail_whenChangeEmail_thenThrowCustomerException() {

        String phoneNumber = PHONE_NUMBER;
        String email = "existingemail@example.com";
        EmailRequestDto newEmail = new EmailRequestDto(email);
        Customer existingCustomer = new Customer();

        when(customerRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.of(customer));
        when(customerRepository.existsByEmail(email)).thenReturn(true);

        assertThrows(CustomerException.class, () -> customerService.updateEmail(newEmail, phoneNumber));
        verify(customerRepository, times(1)).findByPhoneNumber(phoneNumber);
        verify(customerRepository, times(1)).existsByEmail(email);
        verify(customerRepository, never()).save(existingCustomer);
    }

    @Test
    @DisplayName("Тест для добавления email OK")
    void givenPhoneNumberIsCorrect_whenAddEmail_thenSaveToRepository() {

        when(customerRepository.findByPhoneNumber(EXCEPTED_PHONE)).thenReturn(Optional.of(customer));
        when(customerRepository.existsByEmail(TEST_EMAIL)).thenReturn(false);

        EmailRequestDto currentEmail = new EmailRequestDto();
        currentEmail.setEmail(TEST_EMAIL);

        assertDoesNotThrow(() -> customerService.addEmail(currentEmail, EXCEPTED_PHONE));

        assertEquals(TEST_EMAIL, customer.getEmail());
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    @DisplayName("Тест для добавления email NotOK")
    void givenExistedEmail_whenAddEmail_thenDoNotSaveToRepository() {

        String phoneNumber = PHONE_NUMBER;
        String email = "existedemail@example.com";
        EmailRequestDto newEmail = new EmailRequestDto(email);

        when(customerRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.of(customer));
        when(customerRepository.existsByEmail(email)).thenReturn(true);

        assertThrows(CustomerException.class, () -> customerService.addEmail(newEmail, phoneNumber));
        verify(customerRepository, times(1)).findByPhoneNumber(phoneNumber);
        verify(customerRepository, times(1)).existsByEmail(email);
        verify(customerRepository, never()).save(customer);
    }

    @Test
    @DisplayName("Тест для добавления email HaveNotPhoneNumber")
    void givenPhoneNumberDoesNotExist_whenAddEmail_thenThrowException() {

        EmailRequestDto currentEmail = new EmailRequestDto();
        currentEmail.setEmail(TEST_EMAIL);

        when(customerRepository.findByPhoneNumber("HaveNotPhoneNumber")).thenReturn(Optional.empty());

        assertThrows(CustomerException.class, () -> customerService.addEmail(currentEmail, "HaveNotPhoneNumber"));
        verify(customerRepository, never()).save(any());
        verify(customerRepository, never()).existsByEmail(any());
    }

    @Test
    void givenValidCustomerId_whenGetFullName_thenCustomerFullNameResponseDto() {

        CustomerFullNameResponseDto expectedResponseDto = new CustomerFullNameResponseDto("Ivan", "Ivanov", "Ivanovich");

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(customerMapper.toCustomerFullNameResponseDto(customer)).thenReturn(expectedResponseDto);

        CustomerFullNameResponseDto result = customerService.getFullName(customerId.toString());

        assertEquals(expectedResponseDto, result);
        verify(customerRepository).findById(customerId);
        verify(customerMapper).toCustomerFullNameResponseDto(customer);
    }
}
