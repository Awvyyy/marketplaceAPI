[Entities:
User - id, name (unique), password_hash, balance, country, created_at
Product - id, seller_id, title, description, price, stock, created_at
Order - id, buyer_id, seller_id, product_id, order_price, destination, status (ORDERED/SHIPPED/READY_TO_CLAIM/COMPLETED), created_at


User registration - user looks for products - user buys product - user looks for his orders
                                |
                    user creates his own product - user sells his product - user changes order status: ORDERED → SHIPPED → READY_TO_CLAIM → COMPLETED

User can buy product only if:
product in stock,
User has enough balance
User can't buy his own product

User can sell product only if:
User has balance of his product's price (the price of the product will be taken from user's balance, until the order is completed)


*AUTH
POST /auth/registration
IN: name, country, password
OUT: id, name, balance, country

POST /auth/login
IN: name, password
OUT: id, name, balance

*USERS
GET /users/{id}
IN: {id}
OUT: id, name, balance

GET /users/{id}/orders
IN: {id}
OUT: list of user's orders: id, buyer_id, seller_id, product_id, order_price, destination, status (ordered/shipped/ready to claim), created_at

GET /users/{id}/products
IN: {id}
OUT: list of user's products: id, seller_id title, description, price, stock, created_at

*PRODUCTS
GET /products
IN: -
OUT: list of product: seller_id, id, title, description, price, stock, created_at

POST /products
IN: seller_id, title, description, price, stock 
OUT: seller_id, id, title, description, price, stock, created_at

GET /products/{id}
IN: {id}
OUT: seller_id, id, title, description, price, stock, created_at

*ORDERS
POST /orders
IN: buyer_id, product_id, destination
OUT: id, buyer_id, seller_id, product_id, order_price, destination, status (ordered/shipped/ready to claim), created_at

GET /orders/{id}
IN: {id}
OUT: id, buyer_id, seller_id, product_id, order_price, destination, status (ordered/shipped/ready to claim), created_at

PATCH /orders/{id}/status
IN: {id}
OUT: status

]()