package com.example.demo.mapper;

import com.example.demo.model.Order;
import com.example.demo.model.User;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface UserMapper {
    // id, name (unique), password_hash, balance, country, created_at

    @Select("""
SELECT id,
       name,
       password_hash AS passwordHash,
       balance,
       country,
       created_at AS createdAt,
       sales
       FROM users
""")
    List<User> findAll();

    @Select("""
SELECT id,
       name,
       password_hash AS passwordHash,
       balance,
       country,
       created_at AS createdAt,
       sales
       FROM users
WHERE id = #{id}
""")
    User getUserById(Long id);

    @Update("""
UPDATE users
SET balance = #{balance}
WHERE id = #{id}
""")
    int updateBalance(@Param("id") Long id, @Param("balance") BigDecimal balance);

    @Insert("""
        INSERT INTO users (name, password_hash, country)
        VALUES (#{name}, #{passwordHash}, #{country})
    """)
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int createUser(User user);

    @Select("""
SELECT id,
       name,
       password_hash AS passwordHash,
       balance,
       country,
       created_at AS createdAt,
       sales
       FROM users
       WHERE name = #{name}
""")
    User getUserByName(String name);

    @Update("""
UPDATE users
SET sales = #{sales}
WHERE id = #{id}
""")
    int updateSales(@Param("id") Long id, @Param("sales") int sales);
}