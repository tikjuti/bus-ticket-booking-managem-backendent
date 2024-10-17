CREATE PROCEDURE canDriverOperateVehicle(
    IN tripId VARCHAR(255),
    IN employeeId VARCHAR(255),
    OUT isValid BIT
)
BEGIN
    DECLARE counts INT DEFAULT 0;
    DECLARE vehicleId VARCHAR(255);
    DECLARE departureDate DATE;

    -- Lấy mã xe và ngày giờ xuất phát của chuyến đi
    SELECT vehicle_id, departure_date
    INTO vehicleId, departureDate
    FROM trip
    WHERE id = tripId;

    -- Kiểm tra xem tài xế đã được phân công cho xe đó và thời gian phân công vẫn còn hiệu lực
    SELECT COUNT(*) INTO counts
    FROM driver_assignment_for_vehicle
    WHERE employee_id = employeeId
      AND vehicle_id = vehicleId
      AND end_date >= departureDate;

    -- Nếu số lượng lớn hơn 0, tức là tài xế đã được phân công và không hợp lệ
    IF (counts > 0) THEN
        SET isValid = 0; -- Không hợp lệ (đã được phân công)
    ELSE
        SET isValid = 1; -- Hợp lệ (có thể phân công)
    END IF;
END;
