package com.tikjuti.bus_ticket_booking.dto.request.DriverAssignmentForVehicle;

import com.tikjuti.bus_ticket_booking.dto.request.BaseQueryRequest;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;


@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DAFVQueryRequest extends BaseQueryRequest {
    @Pattern(regexp = "^(eq|neq|gt|gte|lt|lte|like|in):[\\w\\s,]*$", message = "FORMAT_FILTER_INVALID")
    String id;
    @Pattern(regexp = "^(eq|neq|gt|gte|lt|lte|like|in):[\\w\\s,]*$", message = "FORMAT_FILTER_INVALID")
    String startDate;
    @Pattern(regexp = "^(eq|neq|gt|gte|lt|lte|like|in):[\\w\\s,]*$", message = "FORMAT_FILTER_INVALID")
    String endDate;
}
