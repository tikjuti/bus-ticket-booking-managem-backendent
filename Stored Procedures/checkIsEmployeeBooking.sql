USE bus_ticket_booking;
DELIMITER //

CREATE PROCEDURE checkIsEmployeeBooking(
    IN employeeId VARCHAR(255),
    OUT isValid BIT
)
BEGIN
    DECLARE counts INT DEFAULT 0;
    
    -- Kiểm tra xem nhân viên có phải là nhân viên đặt vé
    SELECT COUNT(*) INTO counts
    FROM employee 
    JOIN employee_type
    ON employee.employee_type_id = employee_type.id
    WHERE employee.id = employeeId
      AND employee_type.name_employee_type LIKE '%Nhân viên đặt vé%';

    -- Nếu số lượng lớn hơn 0, thì là nhân viên đặt vé (isValid = 1)
    IF (counts > 0) THEN
        SET isValid = 0; --
    ELSE
        SET isValid = 1; --
    END IF;

END //

DELIMITER ;
