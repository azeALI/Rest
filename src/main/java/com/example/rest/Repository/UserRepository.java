package com.example.rest.Repository;

import com.example.rest.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {
    @Query(value = "select * from user where username= ?1" ,nativeQuery = true)
    User findByUsername(String username);

    @Query(value = "select * from user where email= ?1" ,nativeQuery = true)
    User findByEmail(String email);

    @Query(value = "select * from user where role= ?1 order by id asc" ,nativeQuery = true)
    List<User> findAllByRole(String role);

    @Query(value = "select * from user where id!=?1 and email=?2 ",nativeQuery = true)
    User checkSameEmail(long id,String email);
    @Query(value = "select * from user where id!=?1 and username=?2", nativeQuery = true)
    User checkSameUsername(long id,String username);

    @Query(value = "select * from user where id=?1",nativeQuery = true)
    User findById1(long id);

    @Query(value = "select * from user order by id desc limit 1", nativeQuery = true)
    int lastId();
}
