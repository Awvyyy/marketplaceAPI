package com.example.demo.mapper;

import com.example.demo.model.Product;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProductMapper {

    @Select("""
        SELECT id,
               seller_id AS sellerId,
               title,
               description,
               price,
               stock,
               created_at AS createdAt,
               creation_fee AS creationFee,
               creation_fee_refunded AS creationFeeRefunded
        FROM products
        ORDER BY created_at DESC
    """)
    List<Product> findAll();

    @Select("""
        SELECT id,
               seller_id AS sellerId,
               title,
               description,
               price,
               stock,
               created_at AS createdAt,
               creation_fee AS creationFee,
               creation_fee_refunded AS creationFeeRefunded
        FROM products
        WHERE id = #{id}
    """)
    Product findById(Long id);

    @Insert("""
    INSERT INTO products (seller_id, title, description, price, stock, creation_fee, creation_fee_refunded)
    VALUES (#{sellerId}, #{title}, #{description}, #{price}, #{stock}, #{creationFee}, #{creationFeeRefunded})
""")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int createProduct(Product product);

    @Select("""
        SELECT id,
               seller_id AS sellerId,
               title,
               description,
               price,
               stock,
               created_at AS createdAt,
               creation_fee AS creationFee,
               creation_fee_refunded AS creationFeeRefunded
        FROM products
        WHERE seller_id = #{sellerId}
    """)
    List<Product> findAllProductsBySellerId(Long sellerId);

    @Update("""
    UPDATE products
    SET stock = #{stock}
    WHERE id = #{id}
""")
    int updateStock(@Param("id") Long id, @Param("stock") int stock);

    @Update("""
    UPDATE products
    SET creation_fee_refunded = true
    WHERE id = #{id}
      AND creation_fee_refunded = false
""")
    int markCreationFeeRefunded(@Param("id") Long id);
}