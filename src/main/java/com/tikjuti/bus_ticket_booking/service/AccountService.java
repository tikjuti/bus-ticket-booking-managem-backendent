package com.tikjuti.bus_ticket_booking.service;

import com.tikjuti.bus_ticket_booking.dto.request.Account.AccountCreationRequest;
import com.tikjuti.bus_ticket_booking.dto.response.AccountResponse;
import com.tikjuti.bus_ticket_booking.entity.Account;
import com.tikjuti.bus_ticket_booking.enums.AccountRole;
import com.tikjuti.bus_ticket_booking.exception.AppException;
import com.tikjuti.bus_ticket_booking.exception.ErrorCode;
import com.tikjuti.bus_ticket_booking.mapper.AccountMapper;
import com.tikjuti.bus_ticket_booking.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

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

    public List<Account> getAccounts() {return accountRepository.findAll();}

    public AccountResponse getAccount(String accountId)
    {
        return accountMapper
                .toAccountResponse(accountRepository.findById(accountId)
                        .orElseThrow(() -> new RuntimeException("Account not found")));
    }

//    public AccountResponse updateAccount(RouteUpdateRequest request, String routeId)
//    {
//        Route route = routeRepository
//                .findById(routeId)
//                .orElseThrow(() -> new RuntimeException("Route not found"));
//
//        Boolean exitsRoute = routeRepository.checkRoute(
//                request.getDepartureLocation(),
//                request.getArrivalLocation(),
//                request.getDeparturePoint(),
//                request.getArrivalPoint(),
//                request.getDistance(),
//                request.getDuration()
//        );
//        if(!exitsRoute)
//            throw new AppException(ErrorCode.ROUTE_EXISTED);
//
//        routeMapper.updateRoute(route, request);
//
//        return routeMapper
//                .toRouteResponse(routeRepository.save(route));
//    }
//
//    public RouteResponse patchUpdateRoute(RouteUpdateRequest request, String routeId) {
//        Route route = routeRepository
//                .findById(routeId)
//                .orElseThrow(() -> new RuntimeException("Route not found"));
//
//        Boolean existsRoute = routeRepository.checkRoute(
//                request.getDepartureLocation() != null ? request.getDepartureLocation() : route.getDepartureLocation(),
//                request.getArrivalLocation() != null ? request.getArrivalLocation() : route.getArrivalLocation(),
//                request.getDeparturePoint() != null ? request.getDeparturePoint() : route.getDeparturePoint(),
//                request.getArrivalPoint() != null ? request.getArrivalPoint() : route.getArrivalPoint(),
//                request.getDistance() != 0 ? request.getDistance() : route.getDistance(),
//                request.getDuration() != 0 ? request.getDuration() : route.getDuration()
//        );
//
//        if (!existsRoute) {
//            throw new AppException(ErrorCode.ROUTE_EXISTED);
//        }
//
//        if (request.getDepartureLocation() != null) {
//            route.setDepartureLocation(request.getDepartureLocation());
//        }
//
//        if (request.getArrivalLocation() != null) {
//            route.setArrivalLocation(request.getArrivalLocation());
//        }
//
//        if (request.getDeparturePoint() != null) {
//            route.setDeparturePoint(request.getDeparturePoint());
//        }
//
//        if (request.getArrivalPoint() != null) {
//            route.setArrivalPoint(request.getArrivalPoint());
//        }
//
//        if (request.getDistance() != 0 && request.getDistance() > 0) {
//            route.setDistance(request.getDistance());
//        }
//
//        if (request.getDuration() != 0 && request.getDuration() > 0) {
//            route.setDuration(request.getDuration());
//        }
//
//        Route updatedRoute = routeRepository.save(route);
//
//        return routeMapper.toRouteResponse(updatedRoute);
//    }
//
//
//    public void deleteRoute(String routeId) {
//        routeRepository.findById(routeId)
//                .map(route -> {
//                    routeRepository.delete(route);
//                    return true;
//                })
//                .orElseThrow(() -> new RuntimeException("Route not found for ID: " + routeId));
//    }
}
