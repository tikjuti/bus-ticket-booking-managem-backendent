CREATE PROCEDURE checkRoute(
    IN departureLocation NVARCHAR(255),
    IN arrivalLocation NVARCHAR(255),
    IN departurePoint NVARCHAR(255),
    IN arrivalPoint NVARCHAR(255),
    IN distance INT,
    IN duration INT,
    OUT isValid BIT  -- Change this to BIT
)
route: BEGIN
    -- Add your validation logic here
    DECLARE pointExists INT DEFAULT 0;
    
    SET isValid = 0; 

    -- Check if points are valid
    IF (departureLocation = arrivalLocation AND departurePoint = arrivalPoint) THEN
        SET isValid = 0; -- Invalid
        LEAVE route;
    END IF;

    -- Check if route already exists
    SELECT COUNT(*) INTO pointExists
    FROM bus_ticket_booking.route 
    WHERE departure_location = departureLocation 
      AND arrival_location = arrivalLocation 
      AND departure_point = departurePoint 
      AND arrival_point = arrivalPoint;

    IF (pointExists > 0) THEN
        SET isValid = 0; -- Invalid
    ELSE
        SET isValid = 1; -- Valid
    END IF;
END;
