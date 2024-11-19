package org.aston.customerservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aston.customerservice.dto.request.ChangePasswordRequestDto;
import org.aston.customerservice.dto.request.PasswordRecoveryRequestDto;
import org.aston.customerservice.dto.request.UserProfileCreationRequestDto;
import org.aston.customerservice.exception.CustomerException;
import org.aston.customerservice.exception.UserProfileException;
import org.aston.customerservice.mapper.UserProfileMapper;
import org.aston.customerservice.persistent.entity.Customer;
import org.aston.customerservice.persistent.entity.UserProfile;
import org.aston.customerservice.persistent.repository.CustomerRepository;
import org.aston.customerservice.persistent.repository.UserProfileRepository;
import org.aston.customerservice.service.UserProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.UUID;

import static org.aston.customerservice.configuration.ApplicationConstant.MESSAGE_CUSTOMER_NOT_FOUND;
import static org.aston.customerservice.configuration.ApplicationConstant.MESSAGE_INCORRECT_DATA;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final CustomerRepository customerRepository;
    private final UserProfileRepository userProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserProfileMapper userProfileMapper;

    @Override
    public void createUserProfile(UserProfileCreationRequestDto request) {
        UUID customerId = request.getCustomerId();
        log.info("Поиск клиента в базе данных по id: {}", customerId.toString());
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new CustomerException(MESSAGE_CUSTOMER_NOT_FOUND));

        log.info("Проверка наличия профиля у пользователя.");
        if (!userProfileRepository.existsByCustomerCustomerId(customerId)) {
            userProfileRepository.save(userProfileMapper.userProfileCreationDtoToEntity(request, customer));
            log.info("Профиль пользователя успешно создан.");
        } else {
            throw new UserProfileException(HttpStatus.CONFLICT, "Профиль пользователя уже существует!");
        }
    }

    @Override
    public void recoveryPassword(PasswordRecoveryRequestDto requestDto) {
        UserProfile userProfile = userProfileRepository.findByCustomerCustomerId(UUID.fromString(requestDto.getCustomerId())).orElseThrow(
                () -> new CustomerException(MESSAGE_CUSTOMER_NOT_FOUND)
        );

        if (passwordEncoder.matches(requestDto.getPassword(), userProfile.getHashPassword())) {
            throw new UserProfileException(HttpStatus.CONFLICT, "Данный пароль использовался ранее");
        } else {
            userProfile.setHashPassword(passwordEncoder.encode(requestDto.getPassword()));
            userProfileRepository.save(userProfile);
            log.info("Для профиля: {} восстановление пароля прошло успешно", userProfile.getUserProfileId());
        }
    }

    @Override
    public void changePassword(ChangePasswordRequestDto dto, String phoneNumber) {
        if (dto.getOldPassword().equals(dto.getNewPassword())) {
            throw new CustomerException(MESSAGE_INCORRECT_DATA);
        }
        Customer customer = customerRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new CustomerException(MESSAGE_CUSTOMER_NOT_FOUND));
        UserProfile userProfile = userProfileRepository.findByCustomerCustomerId(customer.getCustomerId())
                .orElseThrow(() -> new CustomerException(MESSAGE_CUSTOMER_NOT_FOUND));
        if (passwordEncoder.matches(dto.getOldPassword(), userProfile.getHashPassword())) {
            userProfile.setHashPassword(passwordEncoder.encode(dto.getNewPassword()));
            userProfileRepository.save(userProfile);
            log.info("Пользователь с id = {} успешно сменил пароль", userProfile.getUserProfileId());
        } else {
            throw new UserProfileException(HttpStatus.UNAUTHORIZED, "Неверный пароль");
        }
    }
}
