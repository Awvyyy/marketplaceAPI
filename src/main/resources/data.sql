/*  id, name (unique), password_hash, balance, country, created_at */

INSERT INTO users  (name, password_hash, balance, country)
VALUES ('michael', 'super_hash', 10.00, 'EE')
ON CONFLICT (name) DO NOTHING;

INSERT INTO users (name, password_hash, balance, country)
VALUES ('Agatha', 'super_hashhh2222', 980.00, 'EE')
ON CONFLICT (name) DO NOTHING;

/* id, seller_id, title, description, price, stock, created_at */

INSERT INTO products (seller_id, title, description, price, stock)
VALUES (1, 'iphone 14 pro max', 'new iphone, unboxed', 540.00, 4);

INSERT INTO products (seller_id, title, description, price, stock)
VALUES (1, 'APPLE AIRPODS MAX', 'airpods max, unboxed, new', 490.00, 2);

/* id, buyer_id, seller_id, product_id, order_price, destination, status (ORDERED/SHIPPED/READY_TO_CLAIM/COMPLETED), created_at */

INSERT INTO orders (buyer_id, seller_id, product_id, order_price, destination, status)
VALUES (2, 1, 2, 490.00, 'EE', 'ORDERED');