package com.tikjuti.bus_ticket_booking.dto.request.Account;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountCreationRequest {
    String username;
    String password;
    Role role;

    public enum Role {
        ADMIN, EMPLOYEE, GUEST
    }

}
