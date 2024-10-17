package com.tikjuti.bus_ticket_booking.dto.request.Account;

import com.tikjuti.bus_ticket_booking.enums.AccountRole;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountUpdateRequest {
    String username;
    Set<AccountRole> roles;
}
