package com.Project.UserService.Database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClientsRepository extends JpaRepository<Client, Integer> {

    @Query("SELECT c FROM Client c WHERE c.email = ?1")
    Optional<Client> findByEmail(String clientEmail);

    @Query("SELECT c FROM Client c")
    List<Client> findAll();

    @Modifying
    @Query(value = "INSERT INTO Clients (First_Name, Last_Name, Age, Email, Password, Seller, Admin_status) VALUES (:firstName,:lastName,:age,:email,:password, :seller, :adminStatus)", nativeQuery = true)
    @Transactional
    void insertClient(@Param("firstName") String firstName, @Param("lastName") String lastName, @Param("age") Integer age,
                      @Param("email") String Email, @Param("password") String password, @Param("seller") boolean seller,
                      @Param("adminStatus") boolean adminStatus);

}

