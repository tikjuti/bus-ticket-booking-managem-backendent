DELIMITER //

CREATE PROCEDURE FindTripsUser(
	IN departureLocation VARCHAR(255),
    IN arrivalLocation VARCHAR(255),
    IN departureDate VARCHAR(255)
)
BEGIN
    SELECT *
    FROM trip
    JOIN route
    ON trip.route_id = route.id
    WHERE route.departure_location = departureLocation
    AND route.arrival_location = arrivalLocation
    AND trip.departure_date = departureDate;
END //

DELIMITER ;