-- Xóa database hiện có nếu tồn tại
DROP DATABASE IF EXISTS PhoneCardStoreDB;
CREATE DATABASE PhoneCardStoreDB;
USE PhoneCardStoreDB;

-- Bảng Roles để lưu trữ chức năng của người dùng
CREATE TABLE Roles (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    RoleName VARCHAR(50) NOT NULL UNIQUE
);

-- Tạo bảng Users
CREATE TABLE Users (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    Username VARCHAR(50) NOT NULL UNIQUE,
    Password VARCHAR(255) NOT NULL,
    Fullname VARCHAR(100),
    Email VARCHAR(100) NOT NULL UNIQUE,
    Phone VARCHAR(20),
    Createdat TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    Roleid INT,
    Wallet BIGINT,
    FOREIGN KEY (RoleID) REFERENCES Roles(ID)
);

-- Bảng ProductType để lưu trữ loại sản phẩm
CREATE TABLE ProductType (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    productname VARCHAR(100) NOT NULL,
    price DOUBLE,
    `Describe` TEXT,
    Sold INT
);

-- Bảng Warehouse để lưu trữ thông tin kho
CREATE TABLE Warehouse (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    `Code` VARCHAR(200),
    `Type` INT,
    current_available INT,
    sold INT,
    FOREIGN KEY (`Type`) REFERENCES ProductType(ID)
);

-- Bảng ProductDetail để lưu trữ thông tin chi tiết của sản phẩm
CREATE TABLE productdetail (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    WarehouseID INT,
    Seri VARCHAR(200),
    ManufactureDate DATE,
    ExpiryDate DATE,
    Denomination DOUBLE,
    Provider VARCHAR(100),
    FOREIGN KEY (WarehouseID) REFERENCES Warehouse(ID)
);

-- Bảng PurchaseHistory để lưu trữ lịch sử mua hàng
CREATE TABLE PurchaseHistory (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    userid INT,
    `Type` INT,
    purchasedate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`Type`) REFERENCES ProductType(ID),
    FOREIGN KEY (userid) REFERENCES Users(ID)
);

-- Bảng PurchaseHistoryDetail để lưu trữ chi tiết lịch sử mua hàng
CREATE TABLE PurchaseHistoryDetail (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    PurchaseHistoryID INT,
    `Code` VARCHAR(200),
    Seri VARCHAR(200),
    FOREIGN KEY (PurchaseHistoryID) REFERENCES PurchaseHistory(ID)
);

-- Bảng Feedback để lưu trữ phản hồi
CREATE TABLE Feedback (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    PurchaseHistoryID INT,
    `Describe` TEXT,
    FOREIGN KEY (PurchaseHistoryID) REFERENCES PurchaseHistory(ID)
);

-- Bảng Blogs để lưu trữ các bài viết
CREATE TABLE Blogs (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    `Name` VARCHAR(255),
    `Text` TEXT
);

-- Bảng BlogComment để lưu trữ các bình luận trên bài viết
CREATE TABLE BlogComment (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    UserID INT,
    BlogID INT,
    `Text` TEXT,
    FOREIGN KEY (BlogID) REFERENCES Blogs(ID),
    FOREIGN KEY (UserID) REFERENCES Users(ID)
);

-- Bảng DepositHistory để lưu trữ lịch sử nạp tiền
CREATE TABLE DepositHistory (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    UserID INT,
    Money BIGINT,
    DepositDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (UserID) REFERENCES Users(ID)
);

-- Xóa bảng order nếu tồn tại
DROP TABLE IF EXISTS `order`;

-- Tạo lại bảng order
CREATE TABLE `order` (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT,
    order_status INT,
    warehouseID INT,
    order_date DATE,
    required_date DATE,
    shipped_date DATE,
    FOREIGN KEY (customer_id) REFERENCES Users(ID),
    FOREIGN KEY (warehouseID) REFERENCES Warehouse(ID)
);


-- Chèn dữ liệu mẫu vào bảng Roles
INSERT INTO Roles (RoleName) VALUES ('Admin'), ('User');

-- Chèn dữ liệu mẫu vào bảng Users
INSERT INTO Users (Username, Password, Fullname, Email, Phone, RoleID, Wallet) 
VALUES 
    ('user1', '$2a$10$/fjstiJrV5sZ78E37kRILeAAwbpHY.TO6hIEwUyA7oSsmYtMNh8Vq', 'User One', 'user1@example.com', '1234567890', 2, 0),
    ('user2', '$2a$10$/fjstiJrV5sZ78E37kRILeAAwbpHY.TO6hIEwUyA7oSsmYtMNh8Vq', 'User Two', 'user2@example.com', '1234567891', 2, 0),
    ('user3', '$2a$10$/fjstiJrV5sZ78E37kRILeAAwbpHY.TO6hIEwUyA7oSsmYtMNh8Vq', 'User Three', 'user3@example.com', '1234567892', 2, 0),
    ('admin', '$2a$10$/fjstiJrV5sZ78E37kRILeAAwbpHY.TO6hIEwUyA7oSsmYtMNh8Vq', 'Admin', 'admin@example.com', '1234567892', 1, 0);

-- Chèn dữ liệu mẫu vào bảng ProductType
INSERT INTO ProductType (productname, price, `Describe`, Sold) VALUES
('Netflix', 100000, 'Netflix accounts include gmail and password', 10),
('Viettel', 200000, 'Viettel Card For Mobile Services', 5),
('Vinaphone', 150000, 'Vinaphone Card For Mobile Services', 20),
('Mobilephone', 300000, 'Mobilephone Card For Mobile Services', 8),
('Quizzlet', 250000, 'Quizzlet and Quizzlet Plus Account', 12),
('QuizzletPlus', 300000, 'Quizzlet Plus Account with extra features', 5);

-- Chèn dữ liệu mẫu vào bảng Warehouse
INSERT INTO Warehouse (`Code`, `Type`, current_available, sold) VALUES
('Code001', 1, 50, 10),
('Code002', 2, 30, 5),
('Code003', 3, 20, 15),
('Code004', 4, 40, 8),
('Code005', 5, 25, 12),
('Code006', 6, 15, 5);

-- Chèn dữ liệu mẫu vào bảng ProductDetail
INSERT INTO productdetail (WarehouseID, Seri, ManufactureDate, ExpiryDate, Denomination, Provider) VALUES
(1, 'Seri001', '2023-01-01', '2024-01-01', 100000, 'Netflix'),
(2, 'Seri002', '2023-02-01', '2024-02-01', 200000, 'Viettel'),
(3, 'Seri003', '2023-03-01', '2024-03-01', 150000, 'Vinaphone'),
(4, 'Seri004', '2023-04-01', '2024-04-01', 300000, 'Mobilephone'),
(5, 'Seri005', '2023-05-01', '2024-05-01', 250000, 'Quizzlet'),
(6, 'Seri006', '2023-06-01', '2024-06-01', 300000, 'Quizzlet');

-- Chèn dữ liệu mẫu vào bảng order
INSERT INTO `order` (customer_id, order_status, warehouseID, order_date, required_date, shipped_date) VALUES
(1, 1, 1, '2024-06-01', '2024-06-05', '2024-06-03'),
(2, 2, 2, '2024-06-02', '2024-06-06', '2024-06-04'),
(3, 1, 3, '2024-06-03', '2024-06-07', '2024-06-05'),
(4, 3, 4, '2024-06-04', '2024-06-08', '2024-06-06');
-- (5, 2, 5, '2024-06-05', '2024-06-09', '2024-06-07');

select * from `order`
-- Kiểm tra bảng và dữ liệu
-- select * from warehouse;
-- select * from producttype;
-- SELECT * FROM ProductDetail;
