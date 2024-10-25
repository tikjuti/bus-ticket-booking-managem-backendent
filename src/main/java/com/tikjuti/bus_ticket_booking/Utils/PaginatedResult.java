package com.tikjuti.bus_ticket_booking.Utils;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaginatedResult<T> {
    List<T> contents;
    int totalItems;
    int page;
    int pageSize;
    int totalPages;

    public PaginatedResult(List<T> contents, int totalItems, int page, int pageSize) {
        this.contents = contents;
        this.totalItems = totalItems;
        this.page = page;
        this.pageSize = pageSize;
        this.totalPages = (int) Math.ceil((double) totalItems / pageSize);
    }
}
