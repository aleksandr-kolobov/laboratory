package org.aston.customerservice.service.impl;

import org.aston.customerservice.exception.VerificationException;
import org.aston.customerservice.persistent.entity.Customer;
import org.aston.customerservice.persistent.entity.Verification;
import org.aston.customerservice.persistent.repository.CustomerRepository;
import org.aston.customerservice.persistent.repository.VerificationRepository;
import org.aston.customerservice.service.AbstractServiceUnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@DisplayName("Тест для VerificationServiceImpl")
public class VerificationServiceImplTest extends AbstractServiceUnitTest {

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private VerificationRepository verificationRepository;
    @InjectMocks
    private VerificationServiceImpl verificationService;

    @Test
    @DisplayName("Тест для sendCode")
    public void givenPhoneNumber_whenSendCode_thenSaveCode() {

        UUID customerId = UUID.randomUUID();
        Customer customer = createMockCustomer(customerId);
        String phoneNumber = customer.getPhoneNumber();
        UUID verificationId = UUID.randomUUID();
        Verification verification = new Verification();
        verification.setUserVerificationId(verificationId);
        verification.setSmsBlockSending(Instant.now());
        verification.setCodeExpiration(Instant.now());

        verification.setVerificationCode("123456");

        when(customerRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.of(customer));
        when(verificationRepository.findByCustomerId(customerId)).thenReturn(Optional.of(verification));

        verificationService.sendCode(phoneNumber);

        verify(customerRepository, times(1)).findByPhoneNumber(phoneNumber);
        verify(verificationRepository, times(1)).findByCustomerId(customerId);
        verify(verificationRepository, times(1)).save(verification);
    }

    @Test
    @DisplayName("Тест для checkCode OK")
    public void givenPhoneNumber_whenCheckCode_thenCheckCode() {

        String code = "123456";
        UUID customerId = UUID.randomUUID();
        Customer customer = createMockCustomer(customerId);
        String phoneNumber = customer.getPhoneNumber();
        UUID verificationId = UUID.randomUUID();
        Verification verification = new Verification();
        verification.setUserVerificationId(verificationId);
        verification.setCodeExpiration(Instant.now().plusSeconds(3));
        verification.setVerificationCode(code);

        when(customerRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.of(customer));
        when(verificationRepository.findByCustomerId(customerId)).thenReturn(Optional.of(verification));

        verificationService.checkCode(phoneNumber, code);

        verify(customerRepository, times(1)).findByPhoneNumber(phoneNumber);
        verify(verificationRepository, times(1)).findByCustomerId(customerId);
    }

    @Test
    @DisplayName("Тест для checkCode истек срок")
    public void givenPhoneNumber_whenCheckCode_thenBAD_REQUEST() {

        String code = "123456";
        UUID customerId = UUID.randomUUID();
        Customer customer = createMockCustomer(customerId);
        String phoneNumber = customer.getPhoneNumber();
        UUID verificationId = UUID.randomUUID();
        Verification verification = new Verification();
        verification.setUserVerificationId(verificationId);
        verification.setCodeExpiration(Instant.now().minusSeconds(3));
        verification.setVerificationCode(code);

        when(customerRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.of(customer));
        when(verificationRepository.findByCustomerId(customerId)).thenReturn(Optional.of(verification));

        assertThrows(VerificationException.class, () -> verificationService.checkCode(phoneNumber, code));

        verify(customerRepository, times(1)).findByPhoneNumber(phoneNumber);
        verify(verificationRepository, times(1)).findByCustomerId(customerId);
    }

}
