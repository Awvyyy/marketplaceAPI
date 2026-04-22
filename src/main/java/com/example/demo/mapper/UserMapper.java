package com.example.demo.mapper;

import com.example.demo.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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
       created_at AS createdAt
       FROM users
""")
    List<User> findAll();

    @Select("""
SELECT id,
       name,
       password_hash AS passwordHash,
       balance,
       country,
       created_at AS createdAt
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
}