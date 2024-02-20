-- 1. This procedure applies discounts to products in the database based
-- on sales frequency. If a product has no sales, a $5 discount is applied.
-- For products with up to 3 sales, a $3 discount is applied, and for popular products
-- with more than 3 sales, a $2 discount is applied.
-- This ensures pricing responsiveness to current market conditions.

CREATE PROCEDURE ApplyDiscounts
AS
BEGIN
    DECLARE @ProductID INT;
    DECLARE @SaleCount INT;

    DECLARE discountCursor CURSOR FOR
        SELECT DISTINCT Product_ID
        FROM PRODUCT;

    OPEN discountCursor;

    FETCH NEXT FROM discountCursor INTO @ProductID;

    WHILE @@FETCH_STATUS = 0
    BEGIN
        SELECT @SaleCount = COUNT(1)
        FROM PRODUCT_SALE
        WHERE Product_ID = @ProductID;

        DECLARE @DiscountAmount INT;

        IF @SaleCount = 0
            SET @DiscountAmount = 5;
        ELSE IF @SaleCount <= 3
            SET @DiscountAmount = 3;
        ELSE
            SET @DiscountAmount = 2;

        UPDATE PRODUCT
        SET Price = Price - @DiscountAmount
        WHERE Product_ID = @ProductID;

        FETCH NEXT FROM discountCursor INTO @ProductID;
    END;

    CLOSE discountCursor;
    DEALLOCATE discountCursor;

    PRINT 'Discounts applied successfully.';
END;
go

--testing
DELETE FROM PRODUCT_SALE;
DELETE FROM PRODUCT;

INSERT INTO PRODUCT (Product_ID, Name, Price, Supplier_ID) VALUES (111, 'Product1', 50, 1);
INSERT INTO PRODUCT (Product_ID, Name, Price, Supplier_ID) VALUES (112, 'Product2', 50, 1);
INSERT INTO PRODUCT (Product_ID, Name, Price, Supplier_ID) VALUES (113, 'Product3', 50, 1);

SELECT * FROM PRODUCT;

INSERT INTO PRODUCT_SALE (Sale_ID, Product_ID, Appointment_ID, Amount) VALUES (222, 112, 1, 1);
INSERT INTO PRODUCT_SALE (Sale_ID, Product_ID, Appointment_ID, Amount) VALUES (223, 112, 2, 1);
INSERT INTO PRODUCT_SALE (Sale_ID, Product_ID, Appointment_ID, Amount) VALUES (224, 112, 3, 1);

INSERT INTO PRODUCT_SALE (Sale_ID, Product_ID, Appointment_ID, Amount) VALUES (225, 113, 1, 1);
INSERT INTO PRODUCT_SALE (Sale_ID, Product_ID, Appointment_ID, Amount) VALUES (226, 113, 2, 1);
INSERT INTO PRODUCT_SALE (Sale_ID, Product_ID, Appointment_ID, Amount) VALUES (227, 113, 3, 1);
INSERT INTO PRODUCT_SALE (Sale_ID, Product_ID, Appointment_ID, Amount) VALUES (228, 113, 4, 1);

SELECT * FROM PRODUCT_SALE;

EXECUTE ApplyDiscounts;
SELECT * FROM PRODUCT;



-- 2. This procedure allows us delete only those categories of services which are not very
-- popular with our clients(they were chosen more then 2 times). It is important for us
-- because we should be aware of our clients needs in order to increase profit.

CREATE PROCEDURE DeleteServiceCategory
    @CategoryID INT
AS
BEGIN
    DECLARE @ServiceCount INT;

    IF NOT EXISTS (SELECT 1 FROM SERVICE_CATEGORY WHERE Category_ID = @CategoryID)
    BEGIN
        PRINT 'Service category with ID ' + CAST(@CategoryID AS NVARCHAR(10)) + ' does not exist.';
        RETURN;
    END

    SELECT @ServiceCount = COUNT(1)
    FROM SERVICE_FOR_APPOINTMENT SFA
    JOIN SERVICE S ON SFA.Service_ID = S.Service_ID
    WHERE S.Category_ID = @CategoryID;

    IF @ServiceCount > 2
    BEGIN
        PRINT 'Cannot delete the service category. It is popular with clients.';
    END

    ELSE
    BEGIN
        DELETE FROM SERVICE_FOR_APPOINTMENT
        WHERE Service_ID IN (SELECT Service_ID FROM SERVICE WHERE Category_ID = @CategoryID);

        DELETE FROM SERVICE WHERE Category_ID = @CategoryID;
        DELETE FROM SERVICE_CATEGORY WHERE Category_ID = @CategoryID;
        PRINT 'Service category with ID ' + CAST(@CategoryID AS NVARCHAR(10)) +
              ' deleted successfully as well as all services that belongs to it.';
    END
END;
go

-- testing
SELECT * FROM SERVICE_CATEGORY;
EXECUTE DeleteServiceCategory @CategoryID = 111;

INSERT INTO SERVICE_CATEGORY (Category_ID, Name) VALUES (111, 'Category');
INSERT INTO SERVICE (Service_ID, Name, Duration, Price, Category_ID) VALUES (222, 'Service', 30, 50, 111);
INSERT INTO SERVICE_FOR_APPOINTMENT (Service_ID, Appointment_ID) VALUES (222, 1);
INSERT INTO SERVICE_FOR_APPOINTMENT (Service_ID, Appointment_ID) VALUES (222, 2);
INSERT INTO SERVICE_FOR_APPOINTMENT (Service_ID, Appointment_ID) VALUES (222, 3);

EXECUTE DeleteServiceCategory @CategoryID = 111;

INSERT INTO SERVICE_CATEGORY (Category_ID, Name) VALUES (112, 'Category');
INSERT INTO SERVICE (Service_ID, Name, Duration, Price, Category_ID) VALUES (223, 'Service', 30, 50, 112);
INSERT INTO SERVICE_FOR_APPOINTMENT (Service_ID, Appointment_ID) VALUES (223, 1);

EXECUTE DeleteServiceCategory @CategoryID = 112;



-- 3. This trigger not allow us to update a date in our appointment table if it
-- is already paid, but if we are changing the date it would not allow us to assign
-- to this appointment a hairdresser who is already busy this day. It helps maintain
-- the accuracy and preventing updates that could lead to collapses in the schedule.

CREATE TRIGGER PreventUpdateAppointment
ON APPOINTMENT
INSTEAD OF UPDATE
AS
BEGIN
    IF UPDATE(ADate)
    BEGIN
        IF EXISTS (
        SELECT 1
        FROM inserted i
        WHERE i.Appointment_ID IN (SELECT Appointment_ID FROM PAYMENT)
        )
        BEGIN
            THROW 51000,'Cannot update date of the appointment. Appointment is already paid.', 1;
        END
    END

    IF UPDATE(Hairdresser_ID) AND UPDATE(ADate)
    BEGIN
    IF EXISTS (
        SELECT 1
        FROM inserted i
        WHERE EXISTS (
            SELECT 1
            FROM APPOINTMENT a
            WHERE a.Hairdresser_ID = i.Hairdresser_ID
              AND a.ADate = i.ADate
              AND a.Appointment_ID <> i.Appointment_ID
        )
    )
    BEGIN
        THROW 51000,'Cannot update hairdresser for the appointment. Hairdresser has appointments on the updated date.',1;
    END
    END

    UPDATE APPOINTMENT
    SET ADate = i.ADate, Hairdresser_ID = i.Hairdresser_ID, Client_ID = i.Client_ID
    FROM APPOINTMENT a
    INNER JOIN inserted i ON a.Appointment_ID = i.Appointment_ID;
 END;
go

-- testing
UPDATE APPOINTMENT set ADate = '2024-01-31' where Appointment_ID = 2;

INSERT INTO APPOINTMENT (Appointment_ID, ADate, Hairdresser_ID, Client_ID) VALUES (333, '2022-04-10', 1, 1);
INSERT INTO APPOINTMENT (Appointment_ID, ADate, Hairdresser_ID, Client_ID) VALUES (334, '2022-04-11', 1, 1);

UPDATE APPOINTMENT
SET
    Hairdresser_ID = 1,
    ADate = '2022-04-10'
WHERE
    Appointment_ID = 334;

UPDATE APPOINTMENT
SET
    Hairdresser_ID = 1,
    ADate = '2022-05-10'
WHERE
    Appointment_ID = 334;



-- 4. This trigger would not allow us to fire a hairdresser if he is working less
-- then 3 months because we have a probationary period in our company which is 3 months.
-- Also it prevent us from deleting a popular specialist
-- (who has more then 5 appointment in last month) as we need him in our team.

CREATE TRIGGER PreventDeleteHairdresser
ON HAIRDRESSER
INSTEAD OF DELETE
AS
BEGIN
    IF (
        SELECT COUNT(1)
        FROM APPOINTMENT a
        WHERE a.Hairdresser_ID IN (SELECT Hairdresser_ID FROM DELETED)
          AND a.ADate >= DATEADD(DAY, -30, GETDATE())
    ) >= 5
    BEGIN
        THROW 51000, 'Cannot delete hairdresser. Hairdresser has more then 5 appointments in the last 30 days.', 1;
    END

    IF EXISTS (
    SELECT 1
    FROM DELETED d
    WHERE DATEDIFF(DAY, d.Hire_date, GETDATE()) <= 90
    )
    BEGIN
        THROW 51000, 'Cannot delete hairdresser. Hairdresser has been working for less than 90 days.', 1;
    END

    DELETE FROM APPOINTMENT
    WHERE Hairdresser_ID IN (SELECT Hairdresser_ID FROM DELETED);

    DELETE FROM HAIRDRESSER
    WHERE Hairdresser_ID IN (SELECT Hairdresser_ID FROM DELETED);
    PRINT 'Hairdresser deleted successfully.';
END;
go

-- testing
INSERT INTO HAIRDRESSER (Hairdresser_ID, Hire_date, Name, Salary, Specialty_ID) VALUES (333, '2024-01-01', 'Lorel Castilio', 60000, 2);
SELECT * FROM HAIRDRESSER;
DELETE FROM HAIRDRESSER WHERE Hairdresser_ID = 333;

INSERT INTO HAIRDRESSER (Hairdresser_ID, Hire_date, Name, Salary, Specialty_ID) VALUES (444, '2023-01-01', 'Lorel Castilio', 60000, 2);
SELECT * FROM HAIRDRESSER;

INSERT INTO APPOINTMENT (Appointment_ID, ADate, Hairdresser_ID, Client_ID) VALUES (41, '2024-01-10', 444, 1);
INSERT INTO APPOINTMENT (Appointment_ID, ADate, Hairdresser_ID, Client_ID) VALUES (42, '2024-01-15', 444, 1);
INSERT INTO APPOINTMENT (Appointment_ID, ADate, Hairdresser_ID, Client_ID) VALUES (43, '2024-01-04', 444, 1);
INSERT INTO APPOINTMENT (Appointment_ID, ADate, Hairdresser_ID, Client_ID) VALUES (44, '2024-01-01', 444, 1);
INSERT INTO APPOINTMENT (Appointment_ID, ADate, Hairdresser_ID, Client_ID) VALUES (45, '2024-01-02', 444, 1);
INSERT INTO APPOINTMENT (Appointment_ID, ADate, Hairdresser_ID, Client_ID) VALUES (46, '2024-01-14', 444, 1);
SELECT * FROM APPOINTMENT;

DELETE FROM APPOINTMENT WHERE Hairdresser_ID = 444;

DELETE FROM HAIRDRESSER WHERE Hairdresser_ID = 444;
