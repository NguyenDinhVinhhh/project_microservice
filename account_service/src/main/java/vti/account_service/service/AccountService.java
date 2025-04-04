package vti.account_service.service;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vti.account_service.entity.Account;
import vti.account_service.repository.IAccountRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService implements IAccountService {
    @Autowired
    private IAccountRepository acRepository;

    @Override
    public List<Account> getListAccounts() {
        return acRepository.findAll();
    }
}
