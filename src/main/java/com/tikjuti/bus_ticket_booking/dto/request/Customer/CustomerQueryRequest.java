package com.tikjuti.bus_ticket_booking.dto.request.Customer;

import com.tikjuti.bus_ticket_booking.dto.request.BaseQueryRequest;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;


@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerQueryRequest extends BaseQueryRequest {
    @Pattern(regexp = "^(eq|neq|gt|gte|lt|lte|like|in):[\\w\\s,]*$", message = "FORMAT_FILTER_INVALID")
    String id;
    @Pattern(regexp = "^(eq|like|in):[^:]*$", message = "FORMAT_FILTER_INVALID")
    String customerName;
    @Pattern(regexp = "^(eq|like|in):[^:]*$", message = "FORMAT_FILTER_INVALID")
    String phone;
    @Pattern(regexp = "^(eq|like|in):[^:]*$", message = "FORMAT_FILTER_INVALID")
    String email;
}
