package org.aston.customerservice.service.impl;

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
import org.aston.customerservice.service.AbstractServiceUnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@DisplayName("Тест для UserProfileService")
public class UserProfileServiceImplTest extends AbstractServiceUnitTest {

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private UserProfileRepository userProfileRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserProfileMapper userProfileMapper;
    @InjectMocks
    private UserProfileServiceImpl userProfileService;

    private final UUID customerId = UUID.randomUUID();
    private final Customer customer = createMockCustomer(customerId);
    private final UserProfile userProfile = createMockUserProfile(customer);
    private final String phoneNumber = customer.getPhoneNumber();
    ChangePasswordRequestDto dtoChangePassword =
            new ChangePasswordRequestDto("Password@123", "Password@321");


    @Test
    public void givenPassword_whenChangePassword_thenReturnOk() {

        when(customerRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.ofNullable(customer));
        when(userProfileRepository.findByCustomerCustomerId(customer.getCustomerId())).thenReturn(Optional.of(userProfile));
        when(passwordEncoder.matches(dtoChangePassword.getOldPassword(), userProfile.getHashPassword())).thenReturn(true);
        when(passwordEncoder.encode(dtoChangePassword.getNewPassword()))
                .thenReturn("$2a$12$0kse7JdZFAdQYNUVELumhOXFTDB2bqiYpRKRK52Qf/FbswzHBHbC.");
        when(userProfileRepository.save(userProfile)).thenReturn(userProfile);

        userProfileService.changePassword(dtoChangePassword, phoneNumber);

        verify(customerRepository, atLeast(1)).findByPhoneNumber(phoneNumber);
        verify(userProfileRepository, atLeast(1)).findByCustomerCustomerId(customer.getCustomerId());
        verify(passwordEncoder, atLeast(1)).encode(dtoChangePassword.getNewPassword());
        verify(userProfileRepository, atLeast(1)).save(userProfile);
    }

    @Test
    public void givenPassword_whenChangePassword_thenReturnBadRequest() {

        when(customerRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.ofNullable(customer));
        when(userProfileRepository.findByCustomerCustomerId(customer.getCustomerId())).thenReturn(Optional.of(userProfile));
        when(passwordEncoder.matches(dtoChangePassword.getOldPassword(), userProfile.getHashPassword())).thenReturn(false);

        assertThrows(UserProfileException.class,
                () -> userProfileService.changePassword(dtoChangePassword, phoneNumber));
        verify(userProfileRepository, never()).save(userProfile);
    }

    @Test
    public void givenPassword_whenChangePassword_thenReturnO() {

        when(customerRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.ofNullable(customer));
        when(userProfileRepository.findByCustomerCustomerId(customer.getCustomerId())).thenReturn(Optional.of(userProfile));
        when(passwordEncoder.matches(dtoChangePassword.getOldPassword(), userProfile.getHashPassword())).thenReturn(true);
        when(passwordEncoder.encode(dtoChangePassword.getNewPassword()))
                .thenReturn("$2a$12$0kse7JdZFAdQYNUVELumhOXFTDB2bqiYpRKRK52Qf/FbswzHBHbC.");
        when(userProfileRepository.save(userProfile)).thenReturn(userProfile);

        userProfileService.changePassword(dtoChangePassword, phoneNumber);

        verify(customerRepository, atLeast(1)).findByPhoneNumber(phoneNumber);
        verify(userProfileRepository, atLeast(1)).findByCustomerCustomerId(customer.getCustomerId());
        verify(passwordEncoder, atLeast(1)).encode(dtoChangePassword.getNewPassword());
        verify(userProfileRepository, atLeast(1)).save(userProfile);
    }

    @Test
    public void givenPassword_whenChangePassword_thenReturnB() {

        when(customerRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.ofNullable(customer));
        when(userProfileRepository.findByCustomerCustomerId(customer.getCustomerId())).thenReturn(Optional.of(userProfile));
        when(passwordEncoder.matches(dtoChangePassword.getOldPassword(), userProfile.getHashPassword())).thenReturn(false);

        assertThrows(UserProfileException.class,
                () -> userProfileService.changePassword(dtoChangePassword, phoneNumber));
        verify(userProfileRepository, never()).save(userProfile);
    }

    @Test
    @DisplayName("Тест метода createUserProfile() когда клиент не найден")
    void givenUserProfileCreationRequest_whenCreateUserProfile_thenThrowCustomerException() {

        UserProfileCreationRequestDto userProfileCreationRequestDto =
                new UserProfileCreationRequestDto(customerId, dtoChangePassword.getNewPassword());
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        assertThrows(CustomerException.class, () -> userProfileService.createUserProfile(userProfileCreationRequestDto));
    }

    @Test
    @DisplayName("Тест метода createUserProfile() когда профиль пользователя уже существует")
    void givenUserProfileCreationRequest_whenUserProfileExists_thenThrowUserProfileException() {

        UserProfileCreationRequestDto userProfileCreationRequestDto =
                new UserProfileCreationRequestDto(customerId, dtoChangePassword.getNewPassword());
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(userProfileRepository.existsByCustomerCustomerId(customerId)).thenReturn(true);

        assertThrows(UserProfileException.class, () -> userProfileService.createUserProfile(userProfileCreationRequestDto));
    }

    @Test
    @DisplayName("Тест метода createUserProfile() когда профиль пользователя успешно создан")
    void givenUserProfileCreationRequest_whenUserProfileDoesNotExist_thenCreateNewUserProfile() {
        UserProfileCreationRequestDto request = new UserProfileCreationRequestDto();
        request.setCustomerId(customerId);
        request.setPassword(dtoChangePassword.getNewPassword());

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(userProfileRepository.existsByCustomerCustomerId(customerId)).thenReturn(false);
        when(userProfileMapper.userProfileCreationDtoToEntity(request, customer)).thenReturn(userProfile);

        userProfileService.createUserProfile(request);

        verify(userProfileRepository, times(1)).save(any(UserProfile.class));
    }

    @Test
    @DisplayName("Тест метода passwordRecovery() когда пароль успешно изменен")
    void givenValidRequest_whenPasswordRecovery_thenPasswordIsUpdated() {

        String uuid = "7ccbd32e-cdd7-490c-9446-dd716d236fc5";
        PasswordRecoveryRequestDto requestDto =
                new PasswordRecoveryRequestDto("newPassword", uuid);
        UserProfile userProfile = new UserProfile();
        userProfile.setHashPassword("oldHashedPassword");
        when(userProfileRepository.findByCustomerCustomerId(UUID.fromString(requestDto.getCustomerId()))).thenReturn(Optional.of(userProfile));
        when(passwordEncoder.matches(requestDto.getPassword(), userProfile.getHashPassword())).thenReturn(false);

        userProfileService.recoveryPassword(requestDto);

        verify(userProfileRepository, times(1)).findByCustomerCustomerId(UUID.fromString(requestDto.getCustomerId()));
        verify(passwordEncoder, times(1)).encode(requestDto.getPassword());
        verify(userProfileRepository, times(1)).save(any(UserProfile.class));
    }

    @Test
    @DisplayName("Тест метода passwordRecovery() когда пароли совпадают")
    void givenMatchingPasswords_whenPasswordRecovery_thenRepeatablePasswordExceptionThrown() {
        String uuid = "7ccbd32e-cdd7-490c-9446-dd716d236fc5";
        PasswordRecoveryRequestDto requestDto =
                new PasswordRecoveryRequestDto("existingPassword", uuid);
        UserProfile userProfile = new UserProfile();
        userProfile.setHashPassword("existingHashedPassword");
        when(userProfileRepository.findByCustomerCustomerId(UUID.fromString(requestDto.getCustomerId()))).thenReturn(Optional.of(userProfile));
        when(passwordEncoder.matches(requestDto.getPassword(), userProfile.getHashPassword())).thenReturn(true);

        UserProfileException exception = assertThrows(UserProfileException.class, () -> {
            userProfileService.recoveryPassword(requestDto);
        });

        assertEquals("Данный пароль использовался ранее", exception.getMessage());
    }

    @Test
    public void changePassword_thenReturnOk() {

        ChangePasswordRequestDto dto = new ChangePasswordRequestDto("123456", "1234567");
        String hashPassword = "3453";

        when(customerRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.ofNullable(customer));
        when(userProfileRepository.findByCustomerCustomerId(customer.getCustomerId())).thenReturn(Optional.of(userProfile));
        when(passwordEncoder.matches(dto.getOldPassword(), userProfile.getHashPassword())).thenReturn(true);
        when(passwordEncoder.encode(dto.getNewPassword())).thenReturn(hashPassword);
        when(userProfileRepository.save(userProfile)).thenReturn(userProfile);

        userProfileService.changePassword(dto, phoneNumber);

        verify(customerRepository, atLeast(1)).findByPhoneNumber(phoneNumber);
        verify(userProfileRepository, atLeast(1)).findByCustomerCustomerId(customer.getCustomerId());
        verify(passwordEncoder, atLeast(1)).encode(dto.getNewPassword());
        verify(userProfileRepository, atLeast(1)).save(userProfile);
    }

    @Test
    public void changePassword_thenReturnBadRequest() {

        ChangePasswordRequestDto dto = new ChangePasswordRequestDto("123456", "1234567");
        String hashPassword = "3453";

        when(customerRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.ofNullable(customer));
        when(userProfileRepository.findByCustomerCustomerId(customer.getCustomerId())).thenReturn(Optional.of(userProfile));
        when(passwordEncoder.matches(dto.getOldPassword(), userProfile.getHashPassword())).thenReturn(false);

        assertThrows(UserProfileException.class,
                () -> userProfileService.changePassword(dto, phoneNumber));
        verify(userProfileRepository, never()).save(userProfile);
    }
}
