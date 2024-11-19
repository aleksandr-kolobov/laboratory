package org.aston.customerservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aston.customerservice.dto.request.PhoneNumberRequestDto;
import org.aston.customerservice.dto.response.StatusResponseDto;
import org.aston.customerservice.dto.response.StatusTypeResponseDto;
import org.aston.customerservice.exception.CustomerException;
import org.aston.customerservice.mapper.StatusMapper;
import org.aston.customerservice.persistent.entity.Customer;
import org.aston.customerservice.persistent.repository.CustomerRepository;
import org.aston.customerservice.service.StatusService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.UUID;

import static org.aston.customerservice.configuration.ApplicationConstant.MESSAGE_CUSTOMER_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatusServiceImpl implements StatusService {

    private final CustomerRepository customerRepository;
    private final StatusMapper statusMapper;

    @Override
    public StatusResponseDto getCustomerStatus(PhoneNumberRequestDto phoneNumber) {
        StatusResponseDto responseDto = new StatusResponseDto();
        Customer customer = customerRepository.findByPhoneNumber(phoneNumber.getPhoneNumber()).orElseThrow(
                () -> new CustomerException(MESSAGE_CUSTOMER_NOT_FOUND));

        if (customer.getStatus().getStatusType() == 3 || customer.getStatus().getStatusType() == 4) {
            throw new CustomerException(HttpStatus.FORBIDDEN, "Запрещено. Отсутствуют права доступа к содержимому");
        }

        if (customer.getStatus().getStatusType() == 2) {
            responseDto.setStatusType((short) 2);
            responseDto.setMessage("Ограничение прав доступа на управление СДБО");
        }

        if (customer.getStatus().getStatusType() == 1) {
            responseDto.setStatusType((short) 1);
            responseDto.setMessage("Без ограничений");
        }
        log.info("Успешно получен статус для клиента с номером телефона: {}", phoneNumber.getPhoneNumber());
        return responseDto;
    }

    @Override
    public StatusTypeResponseDto getStatusType(String customerId) {
        log.info("Поиск типа статуса клиента по id: {}", customerId);
        Customer customer = customerRepository.findById(UUID.fromString(customerId))
                .orElseThrow(() -> new CustomerException(HttpStatus.NOT_FOUND, MESSAGE_CUSTOMER_NOT_FOUND)
                );
        log.info("Тип статуса клиента получен: {}", customer.getStatus().getStatusType());
        return statusMapper.statusTypeToDto(customer.getStatus());
    }
}