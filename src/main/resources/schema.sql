/*  id, name (unique), password_hash, balance, country, created_at */

CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(30) NOT NULL UNIQUE,
    password_hash VARCHAR(100) NOT NULL,
    balance DECIMAL(12, 2) DEFAULT 0 NOT NULL  CHECK (balance >= 0),
    country VARCHAR(50) NOT NULL,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
    sales INT NOT NULL DEFAULT 0 CHECK (sales >= 0)
);

/*  id, seller_id, title, description, price, stock, created_at */

CREATE TABLE IF NOT EXISTS products (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    seller_id BIGINT NOT NULL,
    CONSTRAINT fk_products_seller FOREIGN KEY (seller_id) REFERENCES users (id),
    title VARCHAR(60) NOT NULL,
    description TEXT NOT NULL,
    price DECIMAL(12, 2) NOT NULL CHECK (price > 0),
    stock INT NOT NULL DEFAULT 0 CHECK (stock >= 0),
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL
);

/* id, buyer_id, seller_id, product_id, order_price, destination, status (ORDERED/SHIPPED/READY_TO_CLAIM/COMPLETED), created_at */

CREATE TABLE IF NOT EXISTS orders (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    buyer_id BIGINT NOT NULL,
    CONSTRAINT fk_orders_buyer FOREIGN KEY (buyer_id) REFERENCES users (id),
    seller_id BIGINT NOT NULL,
    CONSTRAINT fk_orders_seller FOREIGN KEY (seller_id) REFERENCES users (id),
    product_id BIGINT NOT NULL,
    CONSTRAINT fk_orders_product FOREIGN KEY (product_id) REFERENCES products (id),
    order_price DECIMAL(12, 2) NOT NULL CHECK (order_price > 0),
    destination VARCHAR(100) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ORDERED' CHECK (status IN ('ORDERED', 'SHIPPED', 'READY_TO_CLAIM', 'COMPLETED')),
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
    amount INT NOT NULL CHECK (amount > 0)
);
