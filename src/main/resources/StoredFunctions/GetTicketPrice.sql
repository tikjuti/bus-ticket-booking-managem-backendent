CREATE FUNCTION GetTicketPrice(
	vehicleId VARCHAR(255),
    routeId VARCHAR(255)
)
RETURNS INT
DETERMINISTIC
BEGIN
    DECLARE ticketPrice INT DEFAULT 0;

    -- Get total seats of the vehicle
    SELECT price.ticket_price
    INTO ticketPrice
    FROM route
    JOIN price ON route.id = price.route_id
    JOIN vehicle_type ON price.vehicle_type_id = vehicle_type.id
    JOIN vehicle ON vehicle.vehicle_type_id = vehicle_type.id
    WHERE vehicle.id = vehicleId
    AND route.id = routeId;

    RETURN ticketPrice;
END;