package com.tikjuti.bus_ticket_booking.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BaseQueryRequest {
    @Min(value = 1, message = "PAGE_INVALID")
    int page = 1;

    @Min(value = 1, message = "PAGE_SIZE_MIN_INVALID")
    @Max(value = 100, message = "PAGE_SIZE_MAX_INVALID")
    int pageSize = 10;

    @Pattern(regexp = "^[-+]?[a-zA-Z]+(,[-+]?[a-zA-Z]+)*$", message = "FORMAT_SORT_INVALID")
    String sort;

    String includes;

}
