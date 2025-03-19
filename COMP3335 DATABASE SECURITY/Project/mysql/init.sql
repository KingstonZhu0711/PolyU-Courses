CREATE DATABASE IF NOT EXISTS medtest_lab;

USE medtest_lab;

CREATE TABLE Patients (
    PatientID INT AUTO_INCREMENT PRIMARY KEY,
    RealName VARBINARY(255) NOT NULL,
    account_name VARCHAR(100) NOT NULL,
    DateOfBirth DATE NOT NULL,
    ContactInfo VARBINARY(255),
    InsuranceDetails VARCHAR(100) NOT NULL,
    PasswordHash VARCHAR(255) NOT NULL, 
    Salt VARCHAR(64) NOT NULL,
    RealNameIV VARBINARY(16),
    ContactInfoIV VARBINARY(16),
    UNIQUE (account_name)
);

CREATE TABLE TestsCatalog (
    TestCode VARCHAR(20) PRIMARY KEY,
    TestName VARCHAR(100) NOT NULL,
    Description VARCHAR(100) NOT NULL,
    Cost DECIMAL(10, 2) NOT NULL
);

CREATE TABLE Orders (
    OrderID INT AUTO_INCREMENT PRIMARY KEY,
    PatientID INT NOT NULL,
    TestCode VARCHAR(20) NOT NULL,
    OrderingPhysician VARCHAR(100),
    OrderDate DATETIME DEFAULT CURRENT_TIMESTAMP,
    Status ENUM('Pending', 'Completed', 'Canceled') NOT NULL,
    FOREIGN KEY (PatientID) REFERENCES Patients(PatientID),
    FOREIGN KEY (TestCode) REFERENCES TestsCatalog(TestCode)
);

CREATE TABLE Appointments (
    AppointmentID INT AUTO_INCREMENT PRIMARY KEY,
    PatientID INT NOT NULL,
    AppointmentDate DATETIME NOT NULL,
    FOREIGN KEY (PatientID) REFERENCES Patients(PatientID)
);

CREATE TABLE Results (
    ResultID INT AUTO_INCREMENT PRIMARY KEY,
    OrderID INT NOT NULL,
    ReportURL VARCHAR(255),
    Interpretation VARCHAR(100) NOT NULL,
    ReportingPathologist VARCHAR(100),
    FOREIGN KEY (OrderID) REFERENCES Orders(OrderID)
);

CREATE TABLE Billing (
    BillingID INT AUTO_INCREMENT PRIMARY KEY,
    OrderID INT NOT NULL,
    BilledAmount DECIMAL(10, 2) NOT NULL,
    PaymentStatus ENUM('Paid', 'Unpaid', 'Pending') NOT NULL,
    InsuranceClaimStatus ENUM('Submitted', 'Approved', 'Rejected', 'Pending') NOT NULL,
    FOREIGN KEY (OrderID) REFERENCES Orders(OrderID)
);

CREATE TABLE Staff (
    StaffID INT AUTO_INCREMENT PRIMARY KEY,
    RealName VARBINARY(255) NOT NULL,
    account_name VARCHAR(100) NOT NULL,
    Role ENUM('Pathologist', 'Physician', 'Secretary') NOT NULL,
    ContactInfo VARBINARY(255),
    PasswordHash VARCHAR(255) NOT NULL,
    Salt VARCHAR(64) NOT NULL,
    RealNameIV VARBINARY(16),
    ContactInfoIV VARBINARY(16),
    UNIQUE (account_name)
);

ALTER TABLE Patients ENCRYPTION='Y';
ALTER TABLE TestsCatalog ENCRYPTION='Y';
ALTER TABLE Orders ENCRYPTION='Y';
ALTER TABLE Appointments ENCRYPTION='Y';
ALTER TABLE Results ENCRYPTION='Y';
ALTER TABLE Billing ENCRYPTION='Y';
ALTER TABLE Staff ENCRYPTION='Y';

