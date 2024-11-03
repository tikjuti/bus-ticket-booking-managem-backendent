CREATE PROCEDURE checkIsDriver(
    IN employeeId VARCHAR(255),
    OUT isValid BIT
)
BEGIN
    DECLARE counts INT DEFAULT 0;
    
    -- Kiểm tra xem nhân viên có phải là tài xế
    SELECT COUNT(*) INTO counts
    FROM employee 
    JOIN employee_type
    ON employee.employee_type_id = employee_type.id
    WHERE employee.id = employeeId
      AND employee.status = "ACTIVE"
      AND employee_type.name_employee_type LIKE '%Tài xế%';

    -- Nếu số lượng lớn hơn 0, thì là tài xế (isValid = 1)
    IF (counts > 0) THEN
        SET isValid = 0; -- Là tài xế (hợp lệ)
    ELSE
        SET isValid = 1; -- Không phải là tài xế (không hợp lệ)
    END IF;

END;
