-- 1. This procedure inserts payment for given appointment, it is very
-- useful because it perform all needed calculations for us(payment for
-- products bought and services used) and display amount that needed to be paid for us,
-- if appointment already paid it display appropriate message which will prevent our client from paying extra money.

CREATE OR REPLACE PROCEDURE insertPayment (
    appointmentID INT,
    methodID INT
) AS
    paymentID INT;
    productSum INT := 0;
    serviceSum INT := 0;
    totalAmount INT := 0;
    paidAmount INT := 0;
    remainingAmount INT := 0;
BEGIN
    DECLARE
       appCount INT;
    BEGIN
        SELECT COUNT(*)
        INTO appCount
        FROM APPOINTMENT
        WHERE Appointment_ID = appointmentID;

        IF appCount = 0 THEN
            DBMS_OUTPUT.PUT_LINE('Appointment does not exist.');
            RETURN;
        END IF;
    END;

    SELECT NVL(SUM(ps.Amount * p.Price), 0)
    INTO productSum
    FROM PRODUCT_SALE ps
    JOIN PRODUCT p ON ps.Product_ID = p.Product_ID
    WHERE ps.Appointment_ID = appointmentID;

    SELECT NVL(SUM(s.Price), 0)
    INTO serviceSum
    FROM SERVICE_FOR_APPOINTMENT sfa
    JOIN SERVICE s ON sfa.Service_ID = s.Service_ID
    WHERE sfa.Appointment_ID = appointmentID;

    totalAmount := productSum + serviceSum;

    SELECT NVL(SUM(Amount), 0)
    INTO paidAmount
    FROM PAYMENT
    WHERE Appointment_ID = appointmentID;

    IF paidAmount >= totalAmount THEN
        DBMS_OUTPUT.PUT_LINE('Payment already made for the total amount: ' || totalAmount);
        RETURN;
    ELSE
        remainingAmount := totalAmount - paidAmount;

    SELECT NVL(MAX(Payment_ID), 0) + 1
    INTO paymentID
    FROM PAYMENT;

    INSERT INTO PAYMENT (Payment_ID, Appointment_ID, Method_ID, Amount)
    VALUES (paymentID, appointmentID, methodID, remainingAmount);

    DBMS_OUTPUT.PUT_LINE('Payment successfully inserted. Amount paid: ' || remainingAmount);
    END IF;
END;

-- testing
BEGIN
   insertPayment(1,1);
END;

INSERT INTO APPOINTMENT (Appointment_ID, ADate, Hairdresser_ID, Client_ID) VALUES (111, TO_DATE('2022-04-10', 'YYYY-MM-DD'), 1, 1);
INSERT INTO SERVICE_FOR_APPOINTMENT (Service_ID, Appointment_ID) VALUES (1, 111);
INSERT INTO PRODUCT_SALE (Sale_ID, Product_ID, Appointment_ID, Amount) VALUES (222, 5, 111, 2);

BEGIN
   insertPayment(111,1);
END;

BEGIN
   insertPayment(112,1);
END;



-- 2. This procedure allow us to safely delete a supplier record as well as all
-- products provided by him which can be useful if we would like to terminate a
-- contract with a given supplier for some reason.

CREATE OR REPLACE PROCEDURE deleteSupplier(
    supplierID INT
) AS
    productID INT;
    supplierCount INT;

    CURSOR ProductCursor IS
        SELECT Product_ID
        FROM PRODUCT
        WHERE Supplier_ID = supplierID;
BEGIN

    SELECT COUNT(*)
    INTO supplierCount
    FROM SUPPLIER
    WHERE SUPPLIER_ID = supplierID;

    IF supplierCount = 0 THEN
        DBMS_OUTPUT.PUT_LINE('Supplier with ID ' || supplierID || ' does not exist.');
        RETURN;
    END IF;

    FOR ProductRecord IN ProductCursor LOOP
        productID := ProductRecord.Product_ID;

        DELETE FROM PRODUCT_SALE
        WHERE Product_ID = productID;

        DELETE FROM ANALOGOUS_PRODUCT
        WHERE Product1_ID = productID OR Product2_ID = productID;

        DELETE FROM PRODUCT
        WHERE Product_ID = productID;
    END LOOP;

    DELETE FROM SUPPLIER
    WHERE Supplier_ID = supplierID;

    DBMS_OUTPUT.PUT_LINE('Supplier with ID ' || supplierID || ' deleted as well as all his products.');
END;

-- testing
SELECT * FROM SUPPLIER;
BEGIN
    DELETESUPPLIER(10);
END;

INSERT INTO SUPPLIER (Supplier_ID, Name, Contact_number) VALUES (111, 'Supplier', 1234560);
INSERT INTO PRODUCT (Product_ID, Name, Price, Supplier_ID) VALUES (222, 'Product1', 50, 111);
INSERT INTO PRODUCT (Product_ID, Name, Price, Supplier_ID) VALUES (223, 'Product2', 25, 111);

BEGIN
    DELETESUPPLIER(111);
END;



-- 3. This trigger would not allow us to change salary of a hairdresser who works less than six month
-- because it against our policy. We also would not have opportunity to increase salary for the employee
-- who had got less then 2 clients last month as it is not a good result so we can't give him appraisal.
-- But we may increase salary for those who worked harder and had at least 5 clients last month.

CREATE OR REPLACE TRIGGER salaryCheck
BEFORE UPDATE ON HAIRDRESSER
FOR EACH ROW
DECLARE
    hireDate DATE;
    numOfClients INT;
BEGIN
    hireDate := :OLD.HIRE_DATE;
    IF MONTHS_BETWEEN(SYSDATE, hireDate) < 6 THEN
        RAISE_APPLICATION_ERROR(-20001, 'Cannot modify salary. Hairdresser has been working for less than 6 months.');
    END IF;

    SELECT COUNT(*)
    INTO numOfClients
    FROM APPOINTMENT
    WHERE Hairdresser_ID = :NEW.Hairdresser_ID
      AND ADate >= SYSDATE - 30;

    IF :NEW.Salary > :OLD.Salary AND numOfClients < 3 THEN
        RAISE_APPLICATION_ERROR(-20002, 'Cannot increase salary. Hairdresser had less than 3 clients last month.');
    END IF;

    IF :NEW.Salary < :OLD.Salary AND numOfClients > 4 THEN
        RAISE_APPLICATION_ERROR(-20003, 'Cannot decrease salary. Hairdresser had more than 4 clients last month.');
    END IF;
END;

-- testing
UPDATE HAIRDRESSER SET Salary = 5000000 WHERE Hairdresser_ID = 1;

INSERT INTO HAIRDRESSER (Hairdresser_ID, Hire_date, Name, Salary, Specialty_ID) VALUES (222, TO_DATE('2024-01-01', 'YYYY-MM-DD'), 'Frida Kitting', 57000, 1);
UPDATE HAIRDRESSER SET Salary = 5000000 WHERE Hairdresser_ID = 222;



-- 4. Following trigger would prevent us from deleting clients who are ours regular customers(had at least 3 appointments last month)
-- and those who have appointments today, it is useful because it will prevent us from having collapses and we would not lost contacts of
-- our regular clients.

CREATE OR REPLACE TRIGGER PreventClientDeletion
BEFORE DELETE ON CLIENT
FOR EACH ROW
DECLARE
    numOfAppointments INT;
BEGIN
    SELECT COUNT(*)
    INTO numOfAppointments
    FROM APPOINTMENT
    WHERE Client_ID = :OLD.Client_ID
    AND ADate >= SYSDATE - 30;

    IF numOfAppointments > 3 THEN
        RAISE_APPLICATION_ERROR(-20001, 'Cannot delete client. More than 3 appointments in the last 30 days.');
    END IF;

    SELECT COUNT(*)
    INTO numOfAppointments
    FROM APPOINTMENT
    WHERE Client_ID = :OLD.Client_ID
        AND TRUNC(ADate) = TRUNC(SYSDATE);

    IF numOfAppointments > 0 THEN
        RAISE_APPLICATION_ERROR(-20002, 'Cannot delete client. Client has an appointment scheduled for today.');
    END IF;
END;

-- testing
INSERT INTO APPOINTMENT (Appointment_ID, ADate, Hairdresser_ID, Client_ID) VALUES (555, TO_DATE('2024-01-10', 'YYYY-MM-DD'), 1, 1);
INSERT INTO APPOINTMENT (Appointment_ID, ADate, Hairdresser_ID, Client_ID) VALUES (556, TO_DATE('2024-01-15', 'YYYY-MM-DD'), 2, 1);
INSERT INTO APPOINTMENT (Appointment_ID, ADate, Hairdresser_ID, Client_ID) VALUES (557, TO_DATE('2024-01-20', 'YYYY-MM-DD'), 3, 1);
INSERT INTO APPOINTMENT (Appointment_ID, ADate, Hairdresser_ID, Client_ID) VALUES (558, TO_DATE('2024-01-05', 'YYYY-MM-DD'), 4, 1);

DELETE CLIENT WHERE CLIENT_ID = 1;

INSERT INTO APPOINTMENT (Appointment_ID, ADate, Hairdresser_ID, Client_ID) VALUES (559, SYSDATE, 1, 5);
DELETE CLIENT WHERE CLIENT_ID = 5;