
USE bus_ticket_booking;
DELIMITER //

CREATE PROCEDURE checkPosition(
    IN p_vehicle_id VARCHAR(255),      -- ID của xe
    IN p_seat_id VARCHAR(255),         -- ID của ghế đang sửa
    IN p_new_position VARCHAR(50), -- Vị trí mới mà người dùng muốn cập nhật
    OUT p_is_exists BIT
)
position: BEGIN
    DECLARE count_positions INT DEFAULT 0;

    -- Check if position already exists
   SELECT COUNT(*) INTO count_positions
    FROM seat
    WHERE vehicle_id = p_vehicle_id
      AND position = p_new_position
      AND id != p_seat_id; -- Loại trừ chính ghế đang sửa

    IF count_positions > 0 THEN
        SET p_is_exists = 1;
    ELSE
        SET p_is_exists = 0;
    END IF;
END //

DELIMITER ;
