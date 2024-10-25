package com.tikjuti.bus_ticket_booking.dto.request.Price;

import com.tikjuti.bus_ticket_booking.dto.request.BaseQueryRequest;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;


@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PriceQueryRequest extends BaseQueryRequest {
    @Pattern(regexp = "^(eq|neq|gt|gte|lt|lte|like|in):[\\w\\s,]*$", message = "FORMAT_FILTER_INVALID")
    String id;
    @Pattern(regexp = "^(eq|neq|gt|gte|lt|lte):[^:]*$", message = "FORMAT_FILTER_INVALID")
    String ticketPrice;
}
