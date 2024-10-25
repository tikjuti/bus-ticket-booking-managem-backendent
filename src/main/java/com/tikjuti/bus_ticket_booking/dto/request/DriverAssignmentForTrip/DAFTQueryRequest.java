package com.tikjuti.bus_ticket_booking.dto.request.DriverAssignmentForTrip;

import com.tikjuti.bus_ticket_booking.dto.request.BaseQueryRequest;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;


@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DAFTQueryRequest extends BaseQueryRequest {
    @Pattern(regexp = "^(eq|neq|gt|gte|lt|lte|like|in):[\\w\\s,]*$", message = "FORMAT_FILTER_INVALID")
    String id;
}
