USE bus_ticket_booking

DELIMITER //

CREATE PROCEDURE createRoute(
    IN departureLocation NVARCHAR(255),
    IN arrivalLocation NVARCHAR(255),
    IN departurePoint NVARCHAR(255),
    IN arrivalPoint NVARCHAR(255),
    IN distance INT,
    IN duration INT,
    OUT errorMessage NVARCHAR(255)
)
route: BEGIN
    DECLARE newRouteId NVARCHAR(36);
    DECLARE pointExists INT DEFAULT 0;

    -- Khởi tạo biến lỗi
    SET errorMessage = '';
    
    IF (departureLocation = arrivalLocation AND departurePoint = arrivalPoint) THEN
        SET errorMessage = 'Điểm đi và điểm đến không được giống nhau';
		LEAVE route;
    END IF;
		

    -- Kiểm tra nếu cùng departure_location, arrival_location, departure_point và arrival_point đã tồn tại
    SELECT COUNT(*) INTO pointExists
    FROM bus_ticket_booking.route 
    WHERE departure_location = departureLocation 
      AND arrival_location = arrivalLocation 
      AND departure_point = departurePoint 
      AND arrival_point = arrivalPoint;

    -- Nếu đã tồn tại, không cho phép thêm
    IF (pointExists > 0) THEN
        SET errorMessage = 'Tuyến đường với cùng bến đi và bến đến đã tồn tại.';
    ELSE
        -- Kiểm tra khoảng cách và thời gian hợp lệ
        IF (distance >= 0 AND duration >= 0) THEN
            -- Tạo UUID cho tuyến đường
            SET newRouteId = UUID();
            
            -- Thêm tuyến đường mới
            INSERT INTO bus_ticket_booking.route (id, departure_location, arrival_location, departure_point, arrival_point, distance, duration)
            VALUES (newRouteId, departureLocation, arrivalLocation, departurePoint, arrivalPoint, distance, duration);

            -- Kiểm tra lỗi khi thêm dữ liệu
            IF (ROW_COUNT() = 0) THEN
                SET errorMessage = 'Có lỗi trong lúc thêm dữ liệu';
                ROLLBACK;
            ELSE
                SET errorMessage = 'Thêm tuyến đường thành công';
            END IF;
        ELSE
            SET errorMessage = 'Quảng đường và thời gian phải lớn hơn hoặc bằng 0';
        END IF;
    END IF;
END //

DELIMITER ; 