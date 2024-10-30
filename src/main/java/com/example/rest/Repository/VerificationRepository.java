package com.example.rest.Repository;


import com.example.rest.Model.TempUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationRepository extends JpaRepository<TempUser,String> {

}
