package com.tikjuti.bus_ticket_booking.dto.request.Route;

import com.tikjuti.bus_ticket_booking.dto.request.BaseQueryRequest;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;


@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RouteQueryRequest extends BaseQueryRequest {
    @Pattern(regexp = "^(eq|neq|gt|gte|lt|lte|like|in):[\\w\\s,]*$", message = "FORMAT_FILTER_INVALID")
    String id;
    @Pattern(regexp = "^(eq|neq|gt|gte|lt|lte|like|in):[\\w\\s,]*$", message = "FORMAT_FILTER_INVALID")
    String departureLocation;
    @Pattern(regexp = "^(eq|neq|gt|gte|lt|lte|like|in):[\\w\\s,]*$", message = "FORMAT_FILTER_INVALID")
    String arrivalLocation;
    @Pattern(regexp = "^(eq|neq|gt|gte|lt|lte|like|in):[\\w\\s,]*$", message = "FORMAT_FILTER_INVALID")
    String departurePoint;
    @Pattern(regexp = "^(eq|neq|gt|gte|lt|lte|like|in):[\\w\\s,]*$", message = "FORMAT_FILTER_INVALID")
    String arrivalPoint;
    @Pattern(regexp = "^(eq|neq|gt|gte|lt|lte):[^:]*$", message = "FORMAT_FILTER_INVALID")
    String distance;
    @Pattern(regexp = "^(eq|neq|gt|gte|lt|lte):[^:]*$", message = "FORMAT_FILTER_INVALID")
    String duration;
}
