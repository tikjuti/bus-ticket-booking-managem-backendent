package com.tikjuti.bus_ticket_booking.service;

import com.tikjuti.bus_ticket_booking.Utils.PaginatedResult;
import com.tikjuti.bus_ticket_booking.Utils.QueryableExtensions;
import com.tikjuti.bus_ticket_booking.dto.request.Account.AccountCreationRequest;
import com.tikjuti.bus_ticket_booking.dto.request.Account.AccountQueryRequest;
import com.tikjuti.bus_ticket_booking.dto.request.Account.AccountUpdateRequest;
import com.tikjuti.bus_ticket_booking.dto.response.AccountResponse;
import com.tikjuti.bus_ticket_booking.entity.Account;
import com.tikjuti.bus_ticket_booking.entity.Employee;
import com.tikjuti.bus_ticket_booking.enums.AccountRole;
import com.tikjuti.bus_ticket_booking.exception.AppException;
import com.tikjuti.bus_ticket_booking.exception.ErrorCode;
import com.tikjuti.bus_ticket_booking.mapper.AccountMapper;
import com.tikjuti.bus_ticket_booking.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Account createAccount(AccountCreationRequest request)
    {
        if(accountRepository.existsByUsername(request.getUsername()))
            throw new AppException(ErrorCode.USERNAME_EXISTED);

        Account account = new Account();
        account.setUsername(request.getUsername());
        account.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<AccountRole> rolesEnum = (HashSet<AccountRole>) request.getRoles();
        HashSet<String> roles = new HashSet<>();
        rolesEnum.forEach(role -> roles.add(role.name()));

        account.setRoles(roles);
        return accountRepository.save(account);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public PaginatedResult<Account> getAccounts(AccountQueryRequest queryRequest) {

        Map<String, Object> filterParams = new HashMap<>();

        if (queryRequest.getId() != null) {
            filterParams.put("id", queryRequest.getId());
        }

        if (queryRequest.getUsername() != null) {
            filterParams.put("username", queryRequest.getUsername());
        }

        Specification<Account> spec = Specification.where(QueryableExtensions.<Account>applyIncludes(queryRequest.getIncludes()))
                .and(QueryableExtensions.applyFilters(filterParams))
                .and(QueryableExtensions.applySorting(queryRequest.getSort()));

        return QueryableExtensions.applyPagination(accountRepository, spec, queryRequest.getPage(), queryRequest.getPageSize());
    }

    public AccountResponse getAccount(String accountId)
    {
        return accountMapper
                .toAccountResponse(accountRepository.findById(accountId)
                        .orElseThrow(() -> new RuntimeException("Account not found")));
    }

    public AccountResponse updateAccount(AccountUpdateRequest request, String accountId)
    {
        Account account = accountRepository
                .findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (accountRepository.existsByUsername(request.getUsername()))
            throw new AppException(ErrorCode.USERNAME_EXISTED);

        account.setUsername(request.getUsername());
        HashSet<AccountRole> rolesEnum = (HashSet<AccountRole>) request.getRoles();
        HashSet<String> roles = new HashSet<>();
        rolesEnum.forEach(role -> roles.add(role.name()));

        account.setRoles(roles);

        return accountMapper
                .toAccountResponse(accountRepository.save(account));
    }

    public AccountResponse patchUpdateAccount(AccountUpdateRequest request, String accountId) {
        Account account = accountRepository
                .findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (request.getUsername() != null) {
            if (accountRepository.existsByUsername(request.getUsername()))
                throw new AppException(ErrorCode.USERNAME_EXISTED);
            account.setUsername(request.getUsername());
        }

        if (request.getRoles() != null) {
            HashSet<AccountRole> rolesEnum = (HashSet<AccountRole>) request.getRoles();
            HashSet<String> roles = new HashSet<>();
            rolesEnum.forEach(role -> roles.add(role.name()));
            account.setRoles(roles);
        }

        return accountMapper.toAccountResponse(accountRepository.save(account));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteAccount(String accountId) {
        accountRepository.findById(accountId)
                .map(account -> {
                    accountRepository.delete(account);
                    return true;
                })
                .orElseThrow(() -> new RuntimeException("Account not found for ID: " + accountId));
    }
}
