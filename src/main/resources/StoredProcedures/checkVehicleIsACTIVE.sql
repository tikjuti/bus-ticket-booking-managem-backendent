CREATE PROCEDURE checkVehicleIsACTIVE(
    IN vehicleId VARCHAR(255),
    OUT isValid BIT
)
BEGIN
    DECLARE vehicleStatus VARCHAR(255);

    SELECT status INTO vehicleStatus
        FROM vehicle
        WHERE id = vehicleId;

    IF (vehicleStatus != 'ACTIVE') THEN
        SET isValid = 1;
    ELSE
            SET isValid = 0;
    END IF;
END;
