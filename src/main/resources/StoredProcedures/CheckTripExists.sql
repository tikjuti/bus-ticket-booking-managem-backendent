CREATE PROCEDURE CheckTripExists(
    IN departureDate DATE,
    IN departureTime TIME,
    IN arrivalDate DATE,
    IN arrivalTime TIME,
    IN vehicleId VARCHAR(255),
    IN routeId VARCHAR(255),
    IN tripId VARCHAR(255),
    OUT isValid BIT
)
BEGIN
    DECLARE newDepartureDateTime DATETIME;
    DECLARE newArrivalDateTime DATETIME;
    DECLARE counts INT DEFAULT 0;

    -- Tạo datetime cho thời gian xuất phát và đến
    SET newDepartureDateTime = CAST(CONCAT(departureDate, ' ', departureTime) AS DATETIME);
    SET newArrivalDateTime = CAST(CONCAT(arrivalDate, ' ', arrivalTime) AS DATETIME);
    
    SELECT COUNT(*) INTO counts
    FROM bus_ticket_booking.trip 
    WHERE vehicle_id = vehicleId
	  AND route_id = routeId
      AND ((CAST(CONCAT(departure_date, ' ', departure_time) AS DATETIME) <= newArrivalDateTime 
             AND CAST(CONCAT(arrival_date, ' ', arrival_time) AS DATETIME) >= newDepartureDateTime)
      OR (CAST(CONCAT(arrival_date, ' ', arrival_time) AS DATETIME) >= newDepartureDateTime 
             AND CAST(CONCAT(departure_date, ' ', departure_time) AS DATETIME) <= newArrivalDateTime))
      AND (tripId IS NULL OR id != tripId); -- Kiểm tra tripId

    -- Nếu số lượng lớn hơn 0, thì không hợp lệ (1)
    IF (counts > 0) THEN
        SET isValid = 1; -- Invalid
    ELSE
        SET isValid = 0; -- Valid
    END IF;

END;
