/* USERS */
INSERT INTO users(username, password, ENABLED)
VALUES ('admin', '$2a$10$mlaWy6IDZA6y5ygXfNmcKOCANYYOWGpBHKmVNu860OoJy7E7jujRG', 1), -- 12345
('user', '$2a$10$UTaaXxOa70jZ0rlAxPpcE.SfVFesa6FDPtYhftnT76B3ODfP1v6gC', 1); -- 12345

/* ROLES */
INSERT INTO authorities(user_id, authority)
VALUES (1, 'ROLE_ADMIN'),
(1, 'ROLE_USER'),
(2, 'ROLE_USER');

/* CLIENTS */
INSERT INTO clients(id, name, surname, email, created_at, photo)
VALUES (1, 'Uzumaki', 'Naruto', 'user1@gmail.com', CURRENT_DATE, ''),
       (2, 'Uchiha', 'Sasuke', 'user2@gmail.com', CURRENT_DATE, ''),
       (3, 'Haruno', 'Sakura', 'user3@gmail.com', CURRENT_DATE, ''),
       (4, 'Hatake', 'Kakashi', 'user4@gmail.com', CURRENT_DATE, ''),
       (5, 'Hyuuga', 'Neji', 'user5@gmail.com', CURRENT_DATE, ''),
       (6, 'Rock', 'Lee', 'user6@gmail.com', CURRENT_DATE, ''),
       (7, 'Ten-ten', 'China', 'user7@gmail.com', CURRENT_DATE, ''),
       (8, 'Aburame', 'Shino', 'user8@gmail.com', CURRENT_DATE, ''),
       (9, 'Hyuuga', 'Hinata', 'user9@gmail.com', CURRENT_DATE, ''),
       (10, 'Inuzuka', 'Kiba', 'user10@gmail.com', CURRENT_DATE, ''),
       (11, 'Naara', 'Shikamaru', 'user11@gmail.com', CURRENT_DATE, ''),
       (12, 'Yamanaka', 'Ino', 'user12@gmail.com', CURRENT_DATE, ''),
       (13, 'Akimichi', 'Choji', 'user13@gmail.com', CURRENT_DATE, ''),
       (14, 'Asuma', 'Sarutobi', 'user14@gmail.com', CURRENT_DATE, ''),
       (15, 'Yuuha', 'Kurenai', 'user15@gmail.com', CURRENT_DATE, ''),
       (16, 'Sarutobi', 'Hiruzen', 'user16@gmail.com', CURRENT_DATE, '');

/* PRODUCTS */
INSERT INTO products(name, price, created_at)
VALUES ('Television Panasonic LCD', 159, NOW()),
('Camera Sony DSC-W320', 329, NOW()),
('Smartphone Samsung Galaxy S13', 899, NOW()),
('Smartphone iPhone 12X', 1299, NOW()),
('Smartphone LG G5', 450, NOW()),
('Xiaomi Note 12', 90, NOW()),
('Xiaomi Redmi 10', 465, NOW()),
('Vivo Reno 5', 349, NOW()),
('Oppo F11', 29, NOW()),
('Huawei', 2, NOW());

/* INVOICES */
INSERT INTO invoices(description, information, client_id, created_at)
VALUES ('Office equipments', NULL, 1, NOW());

INSERT INTO invoice_lines(quantity, invoice_id, product_id)
VALUES (1, 1, 1),
(2, 1, 4),
(3, 1, 8),
(5, 1, 6);
