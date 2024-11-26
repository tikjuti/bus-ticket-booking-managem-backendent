package com.tikjuti.bus_ticket_booking.service;

import com.tikjuti.bus_ticket_booking.Utils.PaginatedResult;
import com.tikjuti.bus_ticket_booking.Utils.QueryableExtensions;
import com.tikjuti.bus_ticket_booking.dto.request.PaymentMethod.PaymentMethodCreationRequest;
import com.tikjuti.bus_ticket_booking.dto.request.PaymentMethod.PaymentMethodQueryRequest;
import com.tikjuti.bus_ticket_booking.dto.request.PaymentMethod.PaymentMethodUpdateRequest;
import com.tikjuti.bus_ticket_booking.dto.response.PaymentMethodResponse;
import com.tikjuti.bus_ticket_booking.entity.PaymentMethod;
import com.tikjuti.bus_ticket_booking.enums.AccountRole;
import com.tikjuti.bus_ticket_booking.exception.AppException;
import com.tikjuti.bus_ticket_booking.exception.ErrorCode;
import com.tikjuti.bus_ticket_booking.mapper.PaymentMethodMapper;
import com.tikjuti.bus_ticket_booking.repository.PaymentMethodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Service
public class PaymentMethodService {
    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Autowired
    private PaymentMethodMapper paymentMethodMapper;

    @PreAuthorize("hasRole('ADMIN')")
    public PaymentMethod createPaymentMethod(PaymentMethodCreationRequest request)
    {
        if(paymentMethodRepository.existsByMethodName(request.getMethodName()))
            throw new AppException(ErrorCode.PAYMENT_METHOD_EXISTED);

        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setMethodName(request.getMethodName());

        HashSet<AccountRole> rolesEnum = (HashSet<AccountRole>) request.getRoles();
        HashSet<String> roles = new HashSet<>();
        rolesEnum.forEach(role -> roles.add(role.name()));

        paymentMethod.setRoles(roles);

        return paymentMethodRepository
                .save(paymentMethod);
    }

    public PaginatedResult<PaymentMethod> getPaymentMethods(PaymentMethodQueryRequest queryRequest) {
        Map<String, Object> filterParams = new HashMap<>();

        if (queryRequest.getId() != null)
            filterParams.put("id", queryRequest.getId());
        if (queryRequest.getMethodName() != null)
            filterParams.put("methodName", queryRequest.getMethodName());

        Specification<PaymentMethod> spec = Specification.where(
                        QueryableExtensions.<PaymentMethod>applyIncludes(queryRequest.getIncludes()))
                .and(QueryableExtensions.applyFilters(filterParams))
                .and(QueryableExtensions.applySorting(queryRequest.getSort()));

        return QueryableExtensions.applyPagination(
                paymentMethodRepository, spec, queryRequest.getPage(), queryRequest.getPageSize());
    }

    public PaymentMethodResponse getPaymentMethod(String paymentMethodId)
    {
        return paymentMethodMapper
                .toPaymentMethodResponse(paymentMethodRepository.findById(paymentMethodId)
                .orElseThrow(() -> new RuntimeException("Payment method not found")));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public PaymentMethodResponse updatePaymentMethod(PaymentMethodUpdateRequest request, String paymentMethodId)
    {
        PaymentMethod paymentMethod = paymentMethodRepository
                .findById(paymentMethodId)
                .orElseThrow(() -> new RuntimeException("Payment method not found"));

        if(paymentMethodRepository.existsByMethodName(request.getMethodName()))
            throw new AppException(ErrorCode.PAYMENT_METHOD_EXISTED);

        paymentMethodMapper.updatePaymentMethod(paymentMethod, request);

        return paymentMethodMapper
                .toPaymentMethodResponse(paymentMethodRepository.save(paymentMethod));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public PaymentMethodResponse patchUpdatePaymentMethod(PaymentMethodUpdateRequest request, String paymentMethodId) {
        PaymentMethod paymentMethod = paymentMethodRepository
                .findById(paymentMethodId)
                .orElseThrow(() -> new RuntimeException("Payment method not found"));


        if (request.getMethodName() != null) {
            if(paymentMethodRepository.existsByMethodName(request.getMethodName()))
                throw new AppException(ErrorCode.PAYMENT_METHOD_EXISTED);
            paymentMethod.setMethodName(request.getMethodName());
        }

        if (request.getRoles() != null) {
            HashSet<AccountRole> rolesEnum = (HashSet<AccountRole>) request.getRoles();
            HashSet<String> roles = new HashSet<>();
            rolesEnum.forEach(role -> roles.add(role.name()));
            paymentMethod.setRoles(roles);
        }

        return paymentMethodMapper
                .toPaymentMethodResponse(paymentMethodRepository.save(paymentMethod));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deletePaymentMethod(String paymentMethodId) {
        paymentMethodRepository.findById(paymentMethodId)
                .map(paymentMethod -> {
                    paymentMethodRepository.delete(paymentMethod);
                    return true;
                })
                .orElseThrow(() -> new RuntimeException("Payment method not found for ID: " + paymentMethodId));
    }
}
