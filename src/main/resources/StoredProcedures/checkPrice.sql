CREATE PROCEDURE checkPrice(
    IN vehicleType NVARCHAR(255),
    IN route NVARCHAR(255),
    IN priceId VARCHAR(255),
    OUT isValid BIT
)
BEGIN
    -- Biến để lưu kết quả đếm
    DECLARE count_prices INT DEFAULT 0;
    
    -- Mặc định là hợp lệ (0)
    SET isValid = 0; 
    
    -- Kiểm tra xem có hàng nào tồn tại với vehicleType và route hay không
    SELECT COUNT(*) INTO count_prices
    FROM bus_ticket_booking.price 
    WHERE vehicle_type_id = vehicleType
      AND route_id = route
      AND id != priceId;

    -- Nếu số lượng lớn hơn 0, thì không hợp lệ (1)
    IF (count_prices > 0) THEN
        SET isValid = 1; -- Invalid
    ELSE
        SET isValid = 0; -- Valid
    END IF;
END;
