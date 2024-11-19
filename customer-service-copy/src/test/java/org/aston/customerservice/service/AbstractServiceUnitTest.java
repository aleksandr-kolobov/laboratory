package org.aston.customerservice.service;

import org.aston.customerservice.dto.response.CustomerResponseDto;
import org.aston.customerservice.persistent.entity.*;
import org.aston.customerservice.security.Role;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public abstract class AbstractServiceUnitTest {

    protected Customer createMockCustomer(UUID customerId) {

        return new Customer(
                customerId,
                null,
                new HashSet<>(),
                new ArrayList<>(),
                "Ivan",
                "Ivanov",
                "Ivanovich",
                "79234251212",
                "Ivan@mail.ru",
                'M',
                LocalDate.of(1990, 1, 1),
                new FamilyStatus(),
                (short) 1,
                LocalDate.of(2020, 1, 1),
                "Вопрос",
                "Ответ",
                new Country(),
                new Status((short) 1, "OK"),
                true);
    }

    protected CustomerResponseDto createMockCustomerResponse() {

        return new CustomerResponseDto(
                "Ivan",
                "Ivanov",
                "Ivanovich",
                "79234251212",
                "Ivan@mail.ru",
                "1990-01-01",
                (short) 1,
                "2020-01-01",
                "OK");
    }

    protected UserProfile createMockUserProfile(Customer customer) {

        return new UserProfile(
                UUID.randomUUID(),
                customer,
                "$2a$12$0kse7JdZFAdQYNUVELumhOXFTDB2bqiYpRKRK52Qf/FbswzHBHbC.",
                LocalDate.of(2020, 1, 2),
                new HashSet<Role>());
    }
}
