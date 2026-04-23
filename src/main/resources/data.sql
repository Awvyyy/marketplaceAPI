CREATE EXTENSION IF NOT EXISTS pgcrypto;

TRUNCATE TABLE orders, products, users RESTART IDENTITY CASCADE;

/* users */

INSERT INTO users (name, password_hash, balance, country, sales) VALUES
                                                                     ('michael', crypt('michael123', gen_salt('bf')), 3200.00, 'EE', 7),
                                                                     ('agatha',  crypt('agatha123',  gen_salt('bf')), 1850.00, 'EE', 3),
                                                                     ('oliver',  crypt('oliver123',  gen_salt('bf')), 950.00,  'LV', 2),
                                                                     ('sophia',  crypt('sophia123',  gen_salt('bf')), 4100.00, 'LT', 5),
                                                                     ('liam',    crypt('liam123',    gen_salt('bf')), 720.00,  'DE', 1),
                                                                     ('emma',    crypt('emma123',    gen_salt('bf')), 2600.00, 'PL', 4),
                                                                     ('noah',    crypt('noah123',    gen_salt('bf')), 540.00,  'FI', 0),
                                                                     ('ava',     crypt('ava123',     gen_salt('bf')), 1330.00, 'SE', 2),
                                                                     ('jack',    crypt('jack123',    gen_salt('bf')), 890.00,  'EE', 1),
                                                                     ('mia',     crypt('mia123',     gen_salt('bf')), 3050.00, 'NL', 6),
                                                                     ('lucas',   crypt('lucas123',   gen_salt('bf')), 1110.00, 'FR', 2),
                                                                     ('zoe',     crypt('zoe123',     gen_salt('bf')), 4800.00, 'ES', 8);

/* products */

INSERT INTO products (seller_id, title, description, price, stock) VALUES
                                                                       (1, 'iPhone 14 Pro Max', 'New iPhone, unboxed', 540.00, 4),
                                                                       (1, 'Apple AirPods Max', 'AirPods Max, unboxed, new', 490.00, 2),
                                                                       (1, 'MacBook Air M2', '13-inch, 16GB RAM, 512GB SSD', 980.00, 3),

                                                                       (2, 'Nintendo Switch OLED', 'White edition, excellent condition', 270.00, 5),
                                                                       (2, 'Sony WH-1000XM5', 'Noise cancelling headphones', 310.00, 4),
                                                                       (2, 'iPad 10th Gen', 'Blue, 64GB, Wi-Fi', 420.00, 2),

                                                                       (3, 'Samsung Galaxy S23', 'Factory unlocked, graphite', 510.00, 6),
                                                                       (3, 'Logitech MX Master 3S', 'Wireless productivity mouse', 85.00, 10),
                                                                       (3, 'Kindle Paperwhite', '11th generation, black', 120.00, 7),

                                                                       (4, 'PlayStation 5', 'Disc edition, sealed', 620.00, 3),
                                                                       (4, 'DualSense Controller', 'Midnight Black', 75.00, 9),
                                                                       (4, 'LG 27-inch 4K Monitor', 'IPS display, office/gaming', 330.00, 4),

                                                                       (5, 'Dell XPS 13', 'Compact ultrabook, 16GB RAM', 760.00, 2),
                                                                       (5, 'Mechanical Keyboard', 'Hot-swappable, RGB', 95.00, 8),
                                                                       (5, 'USB-C Docking Station', 'Triple display support', 140.00, 5),

                                                                       (6, 'GoPro Hero 11', 'Action camera with accessories', 360.00, 3),
                                                                       (6, 'DJI Mini 3', 'Drone with remote controller', 690.00, 2),
                                                                       (6, 'Canon EOS M50', 'Mirrorless camera body + lens', 580.00, 2),

                                                                       (7, 'Gaming Chair', 'Ergonomic black/red chair', 210.00, 4),
                                                                       (7, 'Standing Desk', 'Adjustable height, oak top', 390.00, 2),

                                                                       (8, 'Apple Watch Series 9', '45mm midnight aluminum', 340.00, 4),
                                                                       (8, 'Fitbit Charge 6', 'Fitness tracker, coral', 125.00, 6),

                                                                       (9, 'ASUS ROG Laptop', 'Gaming laptop, RTX 4060', 1240.00, 1),
                                                                       (9, 'Razer DeathAdder V3', 'Gaming mouse, wired', 69.00, 10),

                                                                       (10, 'Bose SoundLink Flex', 'Portable bluetooth speaker', 135.00, 5),
                                                                       (10, 'Marshall Emberton II', 'Portable speaker, black', 155.00, 4),

                                                                       (11, 'Google Pixel 8', 'Obsidian, 128GB', 590.00, 3),
                                                                       (11, 'Anker Power Bank 737', 'High-capacity USB-C battery', 150.00, 7),

                                                                       (12, 'Meta Quest 3', 'VR headset, 128GB', 640.00, 2),
                                                                       (12, 'Steam Deck OLED', '512GB handheld console', 720.00, 2);

/* orders */
/* order_price = total price for the whole order */

INSERT INTO orders (buyer_id, seller_id, product_id, order_price, destination, status, amount) VALUES
                                                                                                   (2, 1, 1, 540.00,  'EE', 'ORDERED',         1),
                                                                                                   (3, 1, 2, 490.00,  'LV', 'SHIPPED',         1),
                                                                                                   (4, 1, 3, 1960.00, 'LT', 'READY_TO_CLAIM',  2),

                                                                                                   (1, 2, 4, 270.00,  'EE', 'COMPLETED',       1),
                                                                                                   (5, 2, 5, 620.00,  'DE', 'COMPLETED',       2),
                                                                                                   (6, 2, 6, 420.00,  'PL', 'SHIPPED',         1),

                                                                                                   (7, 3, 7, 510.00,  'FI', 'ORDERED',         1),
                                                                                                   (8, 3, 8, 170.00,  'SE', 'COMPLETED',       2),
                                                                                                   (9, 3, 9, 120.00,  'EE', 'READY_TO_CLAIM',  1),

                                                                                                   (10, 4, 10, 620.00, 'NL', 'COMPLETED',      1),
                                                                                                   (11, 4, 11, 150.00, 'FR', 'SHIPPED',        2),
                                                                                                   (12, 4, 12, 330.00, 'ES', 'ORDERED',        1),

                                                                                                   (1, 5, 13, 760.00, 'EE', 'SHIPPED',         1),
                                                                                                   (2, 5, 14, 190.00, 'EE', 'COMPLETED',       2),
                                                                                                   (3, 5, 15, 140.00, 'LV', 'ORDERED',         1),

                                                                                                   (4, 6, 16, 360.00, 'LT', 'ORDERED',         1),
                                                                                                   (5, 6, 17, 690.00, 'DE', 'READY_TO_CLAIM',  1),
                                                                                                   (7, 6, 18, 580.00, 'FI', 'COMPLETED',       1),

                                                                                                   (8, 7, 19, 210.00, 'SE', 'ORDERED',         1),
                                                                                                   (9, 7, 20, 390.00, 'EE', 'SHIPPED',         1),

                                                                                                   (10, 8, 21, 340.00, 'NL', 'COMPLETED',      1),
                                                                                                   (11, 8, 22, 250.00, 'FR', 'ORDERED',        2),

                                                                                                   (12, 9, 24, 69.00,  'ES', 'COMPLETED',      1),
                                                                                                   (2, 9, 23, 1240.00, 'EE', 'ORDERED',        1),

                                                                                                   (3, 10, 25, 270.00, 'LV', 'SHIPPED',        2),
                                                                                                   (4, 10, 26, 155.00, 'LT', 'READY_TO_CLAIM', 1),

                                                                                                   (5, 11, 27, 590.00, 'DE', 'ORDERED',        1),
                                                                                                   (6, 11, 28, 300.00, 'PL', 'COMPLETED',      2),

                                                                                                   (7, 12, 29, 640.00, 'FI', 'SHIPPED',        1),
                                                                                                   (8, 12, 30, 720.00, 'SE', 'ORDERED',        1);