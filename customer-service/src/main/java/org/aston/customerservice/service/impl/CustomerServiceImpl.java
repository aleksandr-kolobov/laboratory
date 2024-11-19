package org.aston.customerservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aston.customerservice.dto.response.CustomerFullNameResponseDto;
import org.aston.customerservice.dto.request.EmailRequestDto;
import org.aston.customerservice.dto.request.PhoneNumberRequestDto;
import org.aston.customerservice.dto.response.CustomerIdResponseDto;
import org.aston.customerservice.dto.response.CustomerResponseDto;
import org.aston.customerservice.exception.CustomerException;
import org.aston.customerservice.exception.UserProfileException;
import org.aston.customerservice.mapper.CustomerMapper;
import org.aston.customerservice.persistent.entity.Customer;
import org.aston.customerservice.persistent.repository.CustomerRepository;
import org.aston.customerservice.persistent.repository.UserProfileRepository;
import org.aston.customerservice.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;

import static org.aston.customerservice.configuration.ApplicationConstant.MESSAGE_CUSTOMER_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final UserProfileRepository userProfileRepository;
    private final CustomerMapper customerMapper;

    @Override
    public CustomerResponseDto findById(UUID customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerException(MESSAGE_CUSTOMER_NOT_FOUND));
        return customerMapper.toCustomerResponseDto(customer);
    }

    @Override
    public CustomerResponseDto findByPhoneNumber(String phoneNumber) {
        Customer customer = customerRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new CustomerException(MESSAGE_CUSTOMER_NOT_FOUND));
        log.info("Успешно получена информация о клиенте с номером телефона: {}", customer.getPhoneNumber());
        return customerMapper.toCustomerResponseDto(customer);
    }

    @Override
    public CustomerIdResponseDto checkMissRegistration(PhoneNumberRequestDto request) {
        log.info("Поиск клиента в базе данных по номеру телефона: {}. Проверка отсутствия личного кабинета.", request.getPhoneNumber());
        return customerRepository.findByPhoneNumber(request.getPhoneNumber())
                .filter(customer -> !userProfileRepository.existsByCustomerCustomerId(customer.getCustomerId()))
                .map(customerMapper::toCustomerIdDto)
                .orElseThrow(() -> {
                    Optional<Customer> optionalCustomer = customerRepository.findByPhoneNumber(request.getPhoneNumber());
                    return optionalCustomer.isPresent()
                            ? new UserProfileException("У пользователя есть личный кабинет")
                            : new CustomerException("Не являетесь клиентом банка");
                });
    }

    @Override
    public void checkRegistration(PhoneNumberRequestDto request) {
        log.info("Поиск клиента в базе данных по номеру телефона: {}. Проверка наличия личного кабинета.", request.getPhoneNumber());
        Customer customer = customerRepository.findByPhoneNumber(request.getPhoneNumber())
                .orElseThrow(() -> new CustomerException(MESSAGE_CUSTOMER_NOT_FOUND));
        if (!userProfileRepository.existsByCustomerCustomerId(customer.getCustomerId())) {
            throw new UserProfileException(HttpStatus.NOT_FOUND, "Отсутствует личный кабинет");
        }
    }

    @Override
    public void updateEmail(EmailRequestDto email, String phoneNumber) {
        String newEmail = email.getEmail().toLowerCase();
        log.info("Обновление электронной почты клиента с номером: {}. Новый email: {}", phoneNumber, newEmail);
        Customer customer = customerRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new CustomerException(MESSAGE_CUSTOMER_NOT_FOUND));
        if (customer.getEmail().equals(newEmail)) {
            throw new CustomerException(HttpStatus.UNPROCESSABLE_ENTITY, "Этот email уже был установлен Вами ранее!");
        }
        if (!customerRepository.existsByEmail(newEmail)) {
            customer.setEmail(newEmail);
            customerRepository.save(customer);
            log.info("Электронная почта обновлена для клиента с номером телефона: {}. Новый email: {}", phoneNumber, newEmail);
        } else {
            throw new CustomerException(HttpStatus.UNPROCESSABLE_ENTITY, "Email уже используется другим пользователем!");
        }
    }

    @Override
    public void addEmail(EmailRequestDto email, String phoneNumber) {
        String newEmail = email.getEmail().toLowerCase();
        log.info("Добавление электронной почты: {}", newEmail);
        Customer customer = customerRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new CustomerException("Клиент с номером телефона: " + phoneNumber + " не найден!"));
        if (customer.getEmail().equals(newEmail)) {
            throw new CustomerException(HttpStatus.UNPROCESSABLE_ENTITY, "Этот email уже был установлен Вами ранее!");
        }
        if (!customerRepository.existsByEmail(newEmail)) {
            customer.setEmail(newEmail);
            customerRepository.save(customer);
            log.info("Электронная почта успешно внесена для клиента с номером телефона: {}", phoneNumber);
        } else {
            throw new CustomerException(HttpStatus.UNPROCESSABLE_ENTITY, "Email уже используется другим пользователем!");
        }
    }

    @Override
    public CustomerFullNameResponseDto getFullName(String customerId) {
        log.info("Поиск клиента по его id: {}", customerId);
        Customer customer = customerRepository.findById(UUID.fromString(customerId))
                .orElseThrow(() -> new CustomerException(MESSAGE_CUSTOMER_NOT_FOUND));
        log.info("Клиент успешно найден с id: {}.", customerId);
        return customerMapper.toCustomerFullNameResponseDto(customer);
    }
}
