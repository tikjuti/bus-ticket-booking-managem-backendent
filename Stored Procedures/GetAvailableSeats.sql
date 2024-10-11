DELIMITER //

CREATE FUNCTION GetAvailableSeats(vehicleId VARCHAR(255))
RETURNS INT
DETERMINISTIC
BEGIN
    DECLARE totalSeats INT DEFAULT 0;
    DECLARE bookedSeats INT DEFAULT 0;

    -- Get total seats of the vehicle
    SELECT COUNT(*) 
    INTO totalSeats
    FROM vehicle
    JOIN seat
    ON vehicle.id = seat.vehicle_id
    WHERE vehicle.id = vehicleId;

    -- Get the count of booked seats
    SELECT COUNT(*) 
    INTO bookedSeats
    FROM ticket 
    JOIN seat ON ticket.seat_id = seat.id
    JOIN vehicle ON seat.vehicle_id = vehicle.id
    WHERE vehicle.id = vehicleId;

    -- Return the number of available seats
    RETURN totalSeats - bookedSeats;
END //

DELIMITER ;
