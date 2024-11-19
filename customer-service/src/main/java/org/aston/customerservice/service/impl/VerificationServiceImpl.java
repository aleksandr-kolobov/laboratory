package org.aston.customerservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aston.customerservice.exception.CustomerException;
import org.aston.customerservice.exception.VerificationException;
import org.aston.customerservice.persistent.entity.Customer;
import org.aston.customerservice.persistent.entity.Verification;
import org.aston.customerservice.persistent.repository.CustomerRepository;
import org.aston.customerservice.persistent.repository.VerificationRepository;
import org.aston.customerservice.service.VerificationService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class VerificationServiceImpl implements VerificationService {

    private final CustomerRepository customerRepository;
    private final VerificationRepository verificationRepository;

    @Override
    public void sendCode(String phoneNumber) {
        Customer customer = customerRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new CustomerException("Клиент не найден"));
        UUID customerId = customer.getCustomerId();
        Optional<Verification> optionalVerification = verificationRepository.findByCustomerId(customerId);

        Verification verification;
        if (optionalVerification.isPresent()) {
            verification = optionalVerification.get();
            if (verification.getWrongAttemptsCounter() >= 5) {
                throw new VerificationException(HttpStatus.TOO_MANY_REQUESTS, "Превышено количество попыток");
            }
            if (verification.getSmsBlockSending().isAfter(Instant.now())) {
                throw new VerificationException(HttpStatus.SERVICE_UNAVAILABLE, "Заблокирована отправка смс");
            }
        } else {
            verification = new Verification();
            verification.setCustomerId(customerId);
            log.info("Код верификации для пользователя с номером {} создан", customer.getPhoneNumber());
        }

        verification.setVerificationCode("123456");//условно сгенерирован и отправлен

        verification.incrementAttemptsCounter();
        verification.setCodeExpiration(Instant.now().plusSeconds(180));// 3мин
        verification.setSmsBlockSending(Instant.now());
        verification.resetWrongAttemptsCounter();
        verificationRepository.save(verification);
        log.info("Код верификации сохранен");
    }

    @Override
    public void checkCode(String phoneNumber, String code) {
        Customer customer = customerRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new CustomerException("Клиент не найден"));
        Verification verification = verificationRepository.findByCustomerId(customer.getCustomerId())
                .orElseThrow(() -> new VerificationException("Код верификации не найден"));

        if (!verification.getVerificationCode().equals(code)) {
            verification.incrementAttemptsCounter();
            verificationRepository.save(verification);
            throw new VerificationException("Неверный код верификации");
        }

        if (verification.getCodeExpiration().isBefore(Instant.now())) {
            verification.incrementAttemptsCounter();
            verificationRepository.save(verification);
            throw new VerificationException("Код верификации истек");
        }
        verification.resetWrongAttemptsCounter();
        verificationRepository.save(verification);
        log.info("Код верификации успешно проверен");
    }
}