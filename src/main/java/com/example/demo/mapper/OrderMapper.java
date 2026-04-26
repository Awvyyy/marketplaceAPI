package com.example.demo.mapper;

/* id, buyer_id, seller_id, product_id, order_price, destination, status (ORDERED/SHIPPED/READY_TO_CLAIM/COMPLETED), created_at */

import com.example.demo.model.Order;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrderMapper {
    @Select("""
        SELECT id,
               buyer_id AS buyerId,
               seller_id AS sellerId,
               product_id AS productId,
               order_price AS orderPrice,
               destination,
               status,
               amount,
               created_at AS createdAt,
               updated_at AS updatedAt
        FROM orders
        ORDER BY created_at DESC
    """)
    List<Order> findAll();

    @Select("""
        SELECT id,
               buyer_id AS buyerId,
               seller_id AS sellerId,
               product_id AS productId,
               order_price AS orderPrice,
               destination,
               status,
               amount,
               created_at AS createdAt,
               updated_at AS updatedAt
        FROM orders
        WHERE id = #{id}
    """)
    Order findById(Long id);

    @Select("""
        SELECT id,
               buyer_id AS buyerId,
               seller_id AS sellerId,
               product_id AS productId,
               order_price AS orderPrice,
               destination,
               status,
               amount,
               created_at AS createdAt,
               updated_at AS updatedAt
        FROM orders
        WHERE seller_id = #{sellerId}
    """)
    List<Order> findOrdersBySellerId(Long sellerId);

    @Select("""
        SELECT id,
               buyer_id AS buyerId,
               seller_id AS sellerId,
               product_id AS productId,
               order_price AS orderPrice,
               destination,
               status,
               amount,
               created_at AS createdAt,
               updated_at AS updatedAt
        FROM orders
        WHERE buyer_id = #{buyerId}
    """)
    List<Order> findOrdersByBuyerId(Long buyerId);

    @Update("""
    UPDATE orders
    SET status = #{status},
        updated_at = CURRENT_TIMESTAMP
    WHERE id = #{id}
""")
    int updateStatus(@Param("id") Long id, @Param("status") String status);

    @Insert("""
        INSERT INTO orders (buyer_id, seller_id, product_id, order_price, destination, amount)
        VALUES (#{buyerId}, #{sellerId}, #{productId}, #{orderPrice}, #{destination}, #{amount})
    """)
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int createOrder(Order order);

}
