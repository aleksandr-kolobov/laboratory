package org.aston.customerservice.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aston.customerservice.exception.CustomerException;
import org.aston.customerservice.persistent.entity.Customer;
import org.aston.customerservice.persistent.repository.CustomerRepository;
import org.aston.customerservice.security.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new CustomerException(HttpStatus.BAD_REQUEST, "Клиент не найден"));
        log.info("Успешно получена информация для аутентификации  клиента с номером телефона: {}", customer.getPhoneNumber());
        return new CustomUserDetails(customer, customer.getUserProfile());
    }
}
