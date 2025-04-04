package vti.account_service.controller;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vti.account_service.dto.AccountDTO;
import vti.account_service.entity.Account;
import vti.account_service.service.IAccountService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/v1/accounts")
public class AccountController {
    @Autowired
    private IAccountService acService;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public List<AccountDTO> getListAccounts() {

        List<Account> accounts = acService.getListAccounts();

        List<AccountDTO> listAccountDTO = modelMapper.map(
                accounts,
                new TypeToken<List<AccountDTO>>() {}.getType());

        return listAccountDTO;
    }
}
