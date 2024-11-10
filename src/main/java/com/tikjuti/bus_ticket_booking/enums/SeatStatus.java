package com.tikjuti.bus_ticket_booking.enums;

public enum SeatStatus {
    AVAILABLE,    // Ghế còn trống
    LOCKED,       // Ghế đã được chọn nhưng chưa thanh toán
    OCCUPIED,     // Ghế đã được đặt
    MAINTENANCE   // Ghế đang bảo trì
}
