USE bus_ticket_booking;
DELIMITER //

CREATE PROCEDURE CheckDriverAssignmentForTripExists(
    IN tripId VARCHAR(255),
    IN employeeId VARCHAR(255),
    IN driverAssignmentForTripId VARCHAR(255),
    OUT isValid BIT
)
BEGIN
    DECLARE counts INT DEFAULT 0;
    
    DECLARE departureDateTime DATETIME;
    DECLARE arrivalDateTime DATETIME;
    
    -- Lấy ngày và giờ xuất phát, đến của chuyến đi
    SELECT CONCAT(departure_date, ' ', departure_time), CONCAT(arrival_date, ' ', arrival_time)
    INTO departureDateTime, arrivalDateTime
    FROM trip 
    WHERE id = tripId;

    -- Kiểm tra xem tài xế đã được phân công vào chuyến đi khác có trùng thời gian hay không
    SELECT COUNT(*) INTO counts
    FROM driver_assignment_for_trip as pc
    JOIN trip as cd 
    ON pc.trip_id = cd.id
    WHERE pc.employee_id = employeeId 
	  AND cd.id != tripId
      AND ((CONCAT(cd.departure_date, ' ', cd.departure_time) <= arrivalDateTime 
            AND CONCAT(cd.departure_date, ' ', cd.departure_time) >= departureDateTime)
        OR (CONCAT(cd.arrival_date, ' ', cd.arrival_time) >= departureDateTime 
            AND CONCAT(cd.arrival_date, ' ', cd.arrival_time) <= arrivalDateTime))
      AND (driverAssignmentForTripId IS NULL OR pc.id != driverAssignmentForTripId);

    -- Nếu số lượng lớn hơn 0, thì không hợp lệ (1)
    IF (counts > 0) THEN
        SET isValid = 1; -- Không hợp lệ
    ELSE
        SET isValid = 0; -- Hợp lệ
    END IF;

END //

DELIMITER ;
