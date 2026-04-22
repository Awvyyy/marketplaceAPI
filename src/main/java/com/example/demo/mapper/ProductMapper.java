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
               created_at AS createdAt
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
               created_at AS createdAt
        FROM products
        WHERE id = #{id}
    """)
    Product findById(Long id);

    @Insert("""
        INSERT INTO products (seller_id, title, description, price, stock)
        VALUES (#{sellerId}, #{title}, #{description}, #{price}, #{stock})
    """)
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int createProduct(Product product);
}