package org.aston.accountservice.controller.impl.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aston.accountservice.controller.impl.AccountControllerImpl;
import org.aston.accountservice.dto.request.ChangeBalanceAccountRequestDto;
import org.aston.accountservice.dto.request.MasterAccountRequestDto;
import org.aston.accountservice.dto.response.AccountResponseDto;
import org.aston.accountservice.service.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountControllerImpl.class)
@ExtendWith(MockitoExtension.class)
public class AccountControllerImplTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @InjectMocks
    private AccountControllerImpl accountController;
    @MockBean
    private AccountService accountService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void givenValidRequest_whenChangeMasterAccount_thenReturnOk() throws Exception {
        MasterAccountRequestDto dto = new MasterAccountRequestDto("12345678901234567892");

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .patch("/api/v1/account/accounts/update/master_account")
                .content(mapper.writeValueAsString(dto))
                .header("customerId", "ff9a7874-40f8-42a1-ada4-bd49626bd3cc")
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);

        MockMvcBuilders.standaloneSetup(accountController)
                .build()
                .perform(requestBuilder);
    }

    @Test
    public void givenValidRequest_whenChangeBalanceAccount_thenReturnOk() throws Exception {
        ChangeBalanceAccountRequestDto dto = new ChangeBalanceAccountRequestDto("12345678901234567890", BigDecimal.TEN, "+");

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .patch("/api/v1/account/change_balance")
                .content(mapper.writeValueAsString(dto))
                .header("Customer-Id", "123e4567-e89b-12d3-a456-426614174000")
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
    }

    @Test
    public void givenNotValidRequest_whenMasterAccount_thenReturnBadRequest() throws Exception {
        MasterAccountRequestDto dto = new MasterAccountRequestDto("notValid");

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .patch("/api/v1/account/accounts/update/master_account")
                .content(mapper.writeValueAsString(dto))
                .header("сustomerId", "ff9a7874-40f8-42a1-ada4-bd49626bd3cc")
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);

        MockMvcBuilders.standaloneSetup(accountController)
                .build()
                .perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }

    public void givenNotValidRequest_whenChangeBalanceAccount_thenReturnBadRequest() throws Exception {
        ChangeBalanceAccountRequestDto requestDto = new ChangeBalanceAccountRequestDto("notValid", BigDecimal.TEN, "+");

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .patch("/api/v1/account/change_balance")
                .content(mapper.writeValueAsString(requestDto))
                .header("Customer-Id", "123e4567-e89b-12d3-a456-426614174000")
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenCustomerId_whenGetFilteredAccounts_thenReturnOk() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/v1/account/list_account_number")
                .header("Customer-Id", "123e4567-e89b-12d3-a456-426614174000")
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
    }

    @Test
    public void givenEmptyAccountNumber_whenFindAccountInfo_thenReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/v1/account/information/")
                        .header("Customer-Id", "123e4567-e89b-12d3-a456-426614174000")
                        .param("account_number", ""))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenInvalidAccountNumber_whenFindAccountInfo_thenReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/v1/account/information/")
                        .header("Customer-Id", "123e4567-e89b-12d3-a456-426614174000")
                        .param("account_number", "invalid_number"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Неккоректные данные. "));
    }

    @Test
    public void givenValidAccountNumber_whenFindAccountInfo_thenReturnOk() throws Exception {
        String accountNumber = "12345678901234567890";
        String customerId = "123e4567-e89b-12d3-a456-426614174000";

        AccountResponseDto accountResponseDto = AccountResponseDto.builder().accountNumber(accountNumber).build();

        when(accountService.getAccountInfo(accountNumber, customerId)).thenReturn(accountResponseDto);

        mockMvc.perform(get("/api/v1/account/information/")
                        .header("Customer-Id", customerId)
                        .param("account_number", accountNumber))
                .andExpect(content().string(mapper.writeValueAsString(accountResponseDto)))
                .andExpect(status().isOk());
    }
}
