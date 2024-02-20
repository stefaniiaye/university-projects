-- tables
-- Table: ANALOGOUS_PRODUCT
CREATE TABLE ANALOGOUS_PRODUCT (
    Product1_ID int  NOT NULL,
    Product2_ID int  NOT NULL,
    CONSTRAINT ANALOGOUS_PRODUCT_pk PRIMARY KEY  (Product1_ID,Product2_ID)
);

-- Table: APPOINTMENT
CREATE TABLE APPOINTMENT (
    Appointment_ID int  NOT NULL,
    ADate date  NOT NULL,
    Hairdresser_ID int  NOT NULL,
    Client_ID int  NOT NULL,
    CONSTRAINT APPOINTMENT_pk PRIMARY KEY  (Appointment_ID)
);

-- Table: CLIENT
CREATE TABLE CLIENT (
    Client_ID int  NOT NULL,
    Gender_ID int  NOT NULL,
    Name nvarchar(30)  NOT NULL,
    Contact_number nvarchar(10)  NOT NULL,
    CONSTRAINT CLIENT_pk PRIMARY KEY  (Client_ID)
);

-- Table: GENDER
CREATE TABLE GENDER (
    Gender_ID int  NOT NULL,
    Name nvarchar(20)  NOT NULL,
    CONSTRAINT GENDER_pk PRIMARY KEY  (Gender_ID)
);

-- Table: HAIRDRESSER
CREATE TABLE HAIRDRESSER (
    Hairdresser_ID int  NOT NULL,
    Hire_date date  NOT NULL,
    Name nvarchar(30)  NOT NULL,
    Salary int  NOT NULL,
    Specialty_ID int  NOT NULL,
    CONSTRAINT HAIRDRESSER_pk PRIMARY KEY  (Hairdresser_ID)
);

-- Table: METHOD_OF_PAYMENT
CREATE TABLE METHOD_OF_PAYMENT (
    Method_ID int  NOT NULL,
    Name nvarchar(20)  NOT NULL,
    CONSTRAINT METHOD_OF_PAYMENT_pk PRIMARY KEY  (Method_ID)
);

-- Table: PAYMENT
CREATE TABLE PAYMENT (
    Payment_ID int  NOT NULL,
    Appointment_ID int  NOT NULL,
    Method_ID int  NOT NULL,
    Amount int  NOT NULL,
    CONSTRAINT PAYMENT_pk PRIMARY KEY  (Payment_ID)
);

-- Table: PRODUCT
CREATE TABLE PRODUCT (
    Product_ID int  NOT NULL,
    Name nvarchar(50)  NOT NULL,
    Price int  NOT NULL,
    Supplier_ID int  NOT NULL,
    CONSTRAINT PRODUCT_pk PRIMARY KEY  (Product_ID)
);

-- Table: PRODUCT_SALE
CREATE TABLE PRODUCT_SALE (
    Sale_ID int  NOT NULL,
    Product_ID int  NOT NULL,
    Appointment_ID int  NOT NULL,
    Amount int  NOT NULL,
    CONSTRAINT PRODUCT_SALE_pk PRIMARY KEY  (Sale_ID)
);

-- Table: SALGRADE
CREATE TABLE SALGRADE (
    Grade int  NOT NULL,
    HiSal int  NOT NULL,
    LoSal int  NOT NULL,
    CONSTRAINT SALGRADE_pk PRIMARY KEY  (Grade)
);

-- Table: SERVICE
CREATE TABLE SERVICE (
    Service_ID int  NOT NULL,
    Name nvarchar(100)  NOT NULL,
    Duration int  NOT NULL,
    Price int  NOT NULL,
    Category_ID int  NOT NULL,
    CONSTRAINT SERVICE_pk PRIMARY KEY  (Service_ID)
);

-- Table: SERVICE_CATEGORY
CREATE TABLE SERVICE_CATEGORY (
    Category_ID int  NOT NULL,
    Name nvarchar(20)  NOT NULL,
    CONSTRAINT SERVICE_CATEGORY_pk PRIMARY KEY  (Category_ID)
);

-- Table: SERVICE_FOR_APPOINTMENT
CREATE TABLE SERVICE_FOR_APPOINTMENT (
    Service_ID int  NOT NULL,
    Appointment_ID int  NOT NULL,
    CONSTRAINT SERVICE_FOR_APPOINTMENT_pk PRIMARY KEY  (Service_ID,Appointment_ID)
);

-- Table: SPECIALTY
CREATE TABLE SPECIALTY (
    Specialty_ID int  NOT NULL,
    Name nvarchar(20)  NOT NULL,
    Description nvarchar(200)  NOT NULL,
    CONSTRAINT SPECIALTY_pk PRIMARY KEY  (Specialty_ID)
);

-- Table: SUPPLIER
CREATE TABLE SUPPLIER (
    Supplier_ID int  NOT NULL,
    Name nvarchar(20)  NOT NULL,
    Contact_number nvarchar(10)  NOT NULL,
    CONSTRAINT SUPPLIER_pk PRIMARY KEY  (Supplier_ID)
);

-- foreign keys
-- Reference: ANALOGOUS_PRODUCT1_PRODUCT (table: ANALOGOUS_PRODUCT)
ALTER TABLE ANALOGOUS_PRODUCT ADD CONSTRAINT ANALOGOUS_PRODUCT1_PRODUCT
    FOREIGN KEY (Product1_ID)
    REFERENCES PRODUCT (Product_ID);

-- Reference: ANALOGOUS_PRODUCT2_PRODUCT (table: ANALOGOUS_PRODUCT)
ALTER TABLE ANALOGOUS_PRODUCT ADD CONSTRAINT ANALOGOUS_PRODUCT2_PRODUCT
    FOREIGN KEY (Product2_ID)
    REFERENCES PRODUCT (Product_ID);

-- Reference: APPOINTMENT_CLIENT (table: APPOINTMENT)
ALTER TABLE APPOINTMENT ADD CONSTRAINT APPOINTMENT_CLIENT
    FOREIGN KEY (Client_ID)
    REFERENCES CLIENT (Client_ID);

-- Reference: APPOINTMENT_HAIRDRESSER (table: APPOINTMENT)
ALTER TABLE APPOINTMENT ADD CONSTRAINT APPOINTMENT_HAIRDRESSER
    FOREIGN KEY (Hairdresser_ID)
    REFERENCES HAIRDRESSER (Hairdresser_ID);

-- Reference: CLIENT_GENDER (table: CLIENT)
ALTER TABLE CLIENT ADD CONSTRAINT CLIENT_GENDER
    FOREIGN KEY (Gender_ID)
    REFERENCES GENDER (Gender_ID);

-- Reference: HAIRDRESSER_SPECIALITY (table: HAIRDRESSER)
ALTER TABLE HAIRDRESSER ADD CONSTRAINT HAIRDRESSER_SPECIALITY
    FOREIGN KEY (Specialty_ID)
    REFERENCES SPECIALTY (Specialty_ID);

-- Reference: PAYMENT_APPOINTMENT (table: PAYMENT)
ALTER TABLE PAYMENT ADD CONSTRAINT PAYMENT_APPOINTMENT
    FOREIGN KEY (Appointment_ID)
    REFERENCES APPOINTMENT (Appointment_ID);

-- Reference: PAYMENT_METHOD_OF_PAYMENT (table: PAYMENT)
ALTER TABLE PAYMENT ADD CONSTRAINT PAYMENT_METHOD_OF_PAYMENT
    FOREIGN KEY (Method_ID)
    REFERENCES METHOD_OF_PAYMENT (Method_ID);

-- Reference: PRODUCT_SALES_APPOINTMENT (table: PRODUCT_SALE)
ALTER TABLE PRODUCT_SALE ADD CONSTRAINT PRODUCT_SALES_APPOINTMENT
    FOREIGN KEY (Appointment_ID)
    REFERENCES APPOINTMENT (Appointment_ID);

-- Reference: PRODUCT_SALES_PRODUCT (table: PRODUCT_SALE)
ALTER TABLE PRODUCT_SALE ADD CONSTRAINT PRODUCT_SALES_PRODUCT
    FOREIGN KEY (Product_ID)
    REFERENCES PRODUCT (Product_ID);

-- Reference: PRODUCT_SUPPLIER (table: PRODUCT)
ALTER TABLE PRODUCT ADD CONSTRAINT PRODUCT_SUPPLIER
    FOREIGN KEY (Supplier_ID)
    REFERENCES SUPPLIER (Supplier_ID);

-- Reference: SERVICE_FOR_APPOINTMENT_A (table: SERVICE_FOR_APPOINTMENT)
ALTER TABLE SERVICE_FOR_APPOINTMENT ADD CONSTRAINT SERVICE_FOR_APPOINTMENT_A
    FOREIGN KEY (Appointment_ID)
    REFERENCES APPOINTMENT (Appointment_ID);

-- Reference: SERVICE_FOR_APPOINTMENT_S (table: SERVICE_FOR_APPOINTMENT)
ALTER TABLE SERVICE_FOR_APPOINTMENT ADD CONSTRAINT SERVICE_FOR_APPOINTMENT_S
    FOREIGN KEY (Service_ID)
    REFERENCES SERVICE (Service_ID);

-- Reference: SERVICE_SERVICE_CATEGORY (table: SERVICE)
ALTER TABLE SERVICE ADD CONSTRAINT SERVICE_SERVICE_CATEGORY
    FOREIGN KEY (Category_ID)
    REFERENCES SERVICE_CATEGORY (Category_ID);

-- Insert statements for GENDER table
INSERT INTO GENDER (Gender_ID, Name) VALUES (1, 'Male');
INSERT INTO GENDER (Gender_ID, Name) VALUES (2, 'Female');
INSERT INTO GENDER (Gender_ID, Name) VALUES (3, 'Non-Binary');

-- Insert statements for CLIENT table
INSERT INTO CLIENT (Client_ID, Gender_ID, Name, Contact_number) VALUES (1, 2, 'Jennifer Aniston', 5557778888);
INSERT INTO CLIENT (Client_ID, Gender_ID, Name, Contact_number) VALUES (2, 1, 'Brad Pitt', 5559990000);
INSERT INTO CLIENT (Client_ID, Gender_ID, Name, Contact_number) VALUES (3, 2, 'Leonardo DiCaprio', 5552223333);
INSERT INTO CLIENT (Client_ID, Gender_ID, Name, Contact_number) VALUES (4, 1, 'Angelina Jolie', 5554445555);
INSERT INTO CLIENT (Client_ID, Gender_ID, Name, Contact_number) VALUES (5, 2, 'Tom Hanks', 5556667777);
INSERT INTO CLIENT (Client_ID, Gender_ID, Name, Contact_number) VALUES (6, 1, 'Scarlett Johansson', 5558889999);
INSERT INTO CLIENT (Client_ID, Gender_ID, Name, Contact_number) VALUES (7, 2, 'Chris Hemsworth', 5551112222);
INSERT INTO CLIENT (Client_ID, Gender_ID, Name, Contact_number) VALUES (8, 3, 'Jordan Taylor', 5553334444);

-- Insert statements for SPECIALTY table
INSERT INTO SPECIALTY (Specialty_ID, Name, Description) VALUES (1, 'Cutting', 'Expert in hair cutting techniques');
INSERT INTO SPECIALTY (Specialty_ID, Name, Description) VALUES (2, 'Coloring', 'Specialized in hair coloring services');
INSERT INTO SPECIALTY (Specialty_ID, Name, Description) VALUES (3, 'Styling', 'Skilled in various hair styling methods');

-- Insert statements for HAIRDRESSER table
INSERT INTO HAIRDRESSER (Hairdresser_ID, Hire_date, Name, Salary, Specialty_ID) VALUES (1, '2022-01-01', 'Emma Milestone', 50000, 1);
INSERT INTO HAIRDRESSER (Hairdresser_ID, Hire_date, Name, Salary, Specialty_ID) VALUES (2, '2022-02-15', 'David Moa', 55000, 2);
INSERT INTO HAIRDRESSER (Hairdresser_ID, Hire_date, Name, Salary, Specialty_ID) VALUES (3, '2022-03-30', 'Olivia Starlight', 52000, 3);
INSERT INTO HAIRDRESSER (Hairdresser_ID, Hire_date, Name, Salary, Specialty_ID) VALUES (4, '2022-01-07', 'Lorel Castilio', 60000, 2);
INSERT INTO HAIRDRESSER (Hairdresser_ID, Hire_date, Name, Salary, Specialty_ID) VALUES (5, '2022-07-01', 'Frida Kitting', 57000, 1);

-- Insert statements for APPOINTMENT table
INSERT INTO APPOINTMENT (Appointment_ID, ADate, Hairdresser_ID, Client_ID) VALUES (1, '2022-04-10', 1, 1);
INSERT INTO APPOINTMENT (Appointment_ID, ADate, Hairdresser_ID, Client_ID) VALUES (2, '2022-05-15', 2, 2);
INSERT INTO APPOINTMENT (Appointment_ID, ADate, Hairdresser_ID, Client_ID) VALUES (3, '2022-06-20', 3, 3);
INSERT INTO APPOINTMENT (Appointment_ID, ADate, Hairdresser_ID, Client_ID) VALUES (4, '2022-07-05', 4, 4);
INSERT INTO APPOINTMENT (Appointment_ID, ADate, Hairdresser_ID, Client_ID) VALUES (5, '2022-08-15', 5, 5);
INSERT INTO APPOINTMENT (Appointment_ID, ADate, Hairdresser_ID, Client_ID) VALUES (6, '2022-09-20', 1, 6);
INSERT INTO APPOINTMENT (Appointment_ID, ADate, Hairdresser_ID, Client_ID) VALUES (7, '2022-10-10', 2, 7);
INSERT INTO APPOINTMENT (Appointment_ID, ADate, Hairdresser_ID, Client_ID) VALUES (8, '2022-11-15', 3, 8);
INSERT INTO APPOINTMENT (Appointment_ID, ADate, Hairdresser_ID, Client_ID) VALUES (9, '2022-12-20', 4, 2);
INSERT INTO APPOINTMENT (Appointment_ID, ADate, Hairdresser_ID, Client_ID) VALUES (10, '2023-01-05', 5, 3);

-- Insert statements for SERVICE_CATEGORY table
INSERT INTO SERVICE_CATEGORY (Category_ID, Name) VALUES (1, 'Haircut');
INSERT INTO SERVICE_CATEGORY (Category_ID, Name) VALUES (2, 'Coloring');
INSERT INTO SERVICE_CATEGORY (Category_ID, Name) VALUES (3, 'Styling');

-- Insert statements for SERVICE table
INSERT INTO SERVICE (Service_ID, Name, Duration, Price, Category_ID) VALUES (1, 'Trim and Style', 30, 50, 1);
INSERT INTO SERVICE (Service_ID, Name, Duration, Price, Category_ID) VALUES (2, 'Color Highlights', 40, 80, 2);
INSERT INTO SERVICE (Service_ID, Name, Duration, Price, Category_ID) VALUES (3, 'Complex Coloring', 60, 100, 2);
INSERT INTO SERVICE (Service_ID, Name, Duration, Price, Category_ID) VALUES (4, 'Hairdo', 20, 40, 3);

-- Insert statements for SERVICE_FOR_APPOINTMENT table
INSERT INTO SERVICE_FOR_APPOINTMENT (Service_ID, Appointment_ID) VALUES (1, 1);
INSERT INTO SERVICE_FOR_APPOINTMENT (Service_ID, Appointment_ID) VALUES (4, 1);
INSERT INTO SERVICE_FOR_APPOINTMENT (Service_ID, Appointment_ID) VALUES (3, 2);
INSERT INTO SERVICE_FOR_APPOINTMENT (Service_ID, Appointment_ID) VALUES (4, 3);
INSERT INTO SERVICE_FOR_APPOINTMENT (Service_ID, Appointment_ID) VALUES (3, 4);
INSERT INTO SERVICE_FOR_APPOINTMENT (Service_ID, Appointment_ID) VALUES (2, 4);
INSERT INTO SERVICE_FOR_APPOINTMENT (Service_ID, Appointment_ID) VALUES (1, 5);
INSERT INTO SERVICE_FOR_APPOINTMENT (Service_ID, Appointment_ID) VALUES (1, 6);
INSERT INTO SERVICE_FOR_APPOINTMENT (Service_ID, Appointment_ID) VALUES (2, 7);
INSERT INTO SERVICE_FOR_APPOINTMENT (Service_ID, Appointment_ID) VALUES (4, 8);
INSERT INTO SERVICE_FOR_APPOINTMENT (Service_ID, Appointment_ID) VALUES (1, 9);
INSERT INTO SERVICE_FOR_APPOINTMENT (Service_ID, Appointment_ID) VALUES (3, 9);
INSERT INTO SERVICE_FOR_APPOINTMENT (Service_ID, Appointment_ID) VALUES (1, 10);

-- Insert statements for SUPPLIER table
INSERT INTO SUPPLIER (Supplier_ID, Name, Contact_number) VALUES (1, 'Loreal', 1234567890);
INSERT INTO SUPPLIER (Supplier_ID, Name, Contact_number) VALUES (2, 'Olaplex', 9876543210);
INSERT INTO SUPPLIER (Supplier_ID, Name, Contact_number) VALUES (3, 'Lador', 8765432109);

-- Insert statements for PRODUCT table
INSERT INTO PRODUCT (Product_ID, Name, Price, Supplier_ID) VALUES (1, 'Shampoo', 50, 1);
INSERT INTO PRODUCT (Product_ID, Name, Price, Supplier_ID) VALUES (2, 'Conditioner', 25, 1);
INSERT INTO PRODUCT (Product_ID, Name, Price, Supplier_ID) VALUES (3, 'Styling Gel', 10, 3);
INSERT INTO PRODUCT (Product_ID, Name, Price, Supplier_ID) VALUES (4, 'Hair Oil', 15, 2);
INSERT INTO PRODUCT (Product_ID, Name, Price, Supplier_ID) VALUES (5, 'Hair Mask', 50, 3);

-- Insert statements for PRODUCT_SALE table
INSERT INTO PRODUCT_SALE (Sale_ID, Product_ID, Appointment_ID, Amount) VALUES (1, 5, 6, 2);
INSERT INTO PRODUCT_SALE (Sale_ID, Product_ID, Appointment_ID, Amount) VALUES (2, 1, 2, 1);
INSERT INTO PRODUCT_SALE (Sale_ID, Product_ID, Appointment_ID, Amount) VALUES (3, 2, 2, 1);
INSERT INTO PRODUCT_SALE (Sale_ID, Product_ID, Appointment_ID, Amount) VALUES (4, 3, 10, 3);
INSERT INTO PRODUCT_SALE (Sale_ID, Product_ID, Appointment_ID, Amount) VALUES (5, 4, 7, 2);

-- Insert statements for METHOD_OF_PAYMENT table
INSERT INTO METHOD_OF_PAYMENT (Method_ID, Name) VALUES (1, 'Credit Card');
INSERT INTO METHOD_OF_PAYMENT (Method_ID, Name) VALUES (2, 'Cash');

-- Insert statements for PAYMENT table
INSERT INTO PAYMENT (Payment_ID, Appointment_ID, Method_ID, Amount) VALUES (1, 1, 1, 90);
INSERT INTO PAYMENT (Payment_ID, Appointment_ID, Method_ID, Amount) VALUES (2, 2, 2, 175);
INSERT INTO PAYMENT (Payment_ID, Appointment_ID, Method_ID, Amount) VALUES (3, 3, 1, 40);
INSERT INTO PAYMENT (Payment_ID, Appointment_ID, Method_ID, Amount) VALUES (4, 4, 1, 100);
INSERT INTO PAYMENT (Payment_ID, Appointment_ID, Method_ID, Amount) VALUES (5, 4, 1, 80);
INSERT INTO PAYMENT (Payment_ID, Appointment_ID, Method_ID, Amount) VALUES (6, 5, 2, 50);
INSERT INTO PAYMENT (Payment_ID, Appointment_ID, Method_ID, Amount) VALUES (7, 6, 1, 150);
INSERT INTO PAYMENT (Payment_ID, Appointment_ID, Method_ID, Amount) VALUES (8, 7, 1, 110);
INSERT INTO PAYMENT (Payment_ID, Appointment_ID, Method_ID, Amount) VALUES (9, 8, 1, 40);
INSERT INTO PAYMENT (Payment_ID, Appointment_ID, Method_ID, Amount) VALUES (10, 9, 1, 100);
INSERT INTO PAYMENT (Payment_ID, Appointment_ID, Method_ID, Amount) VALUES (11, 9, 2, 50);
INSERT INTO PAYMENT (Payment_ID, Appointment_ID, Method_ID, Amount) VALUES (12, 10, 2, 80);

-- Insert statements for SALGRADE table
INSERT INTO SALGRADE (Grade, HiSal, LoSal) VALUES (1, 50000, 30000);
INSERT INTO SALGRADE (Grade, HiSal, LoSal) VALUES (2, 70000, 50001);
INSERT INTO SALGRADE (Grade, HiSal, LoSal) VALUES (3, 90000, 70001);
