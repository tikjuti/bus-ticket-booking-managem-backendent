package com.tikjuti.bus_ticket_booking.dto.request.Trip;

import com.tikjuti.bus_ticket_booking.dto.request.BaseQueryRequest;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalTime;


@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TripQueryRequest extends BaseQueryRequest {
    @Pattern(regexp = "^(eq|neq|gt|gte|lt|lte|like|in):[\\w\\s,]*$", message = "FORMAT_FILTER_INVALID")
    String id;
    @Pattern(regexp = "^(eq|neq|gt|gte|lt|lte):[^:]*$", message = "FORMAT_FILTER_INVALID")
    String departureDate;
    @Pattern(regexp = "^(eq|neq|gt|gte|lt|lte):[^:]*$", message = "FORMAT_FILTER_INVALID")
    String departureTime;
    @Pattern(regexp = "^(eq|neq|gt|gte|lt|lte):[^:]*$", message = "FORMAT_FILTER_INVALID")
    String arrivalDate;
    @Pattern(regexp = "^(eq|neq|gt|gte|lt|lte):[^:]*$", message = "FORMAT_FILTER_INVALID")
    String arrivalTime;
}
