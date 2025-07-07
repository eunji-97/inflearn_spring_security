package com.cos.security1.repository;

import com.cos.security1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//@Repository JpaRepository를 상속받으면 생략가능
public interface UserRepository extends JpaRepository<User, Integer> {


    User findByUsername(String username);
}
