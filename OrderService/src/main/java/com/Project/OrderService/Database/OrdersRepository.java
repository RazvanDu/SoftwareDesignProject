package com.Project.OrderService.Database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Order, Integer> {

    @Modifying
    @Query(value = "INSERT INTO Orders (address, message, issues, amounts, total) " +
            "VALUES (:address,:message,:issues,:amounts,:total)", nativeQuery = true)
    @Transactional
    void save(@Param("address") String address,
              @Param("message") String message,
              @Param("issues") String issues,
              @Param("amounts") String amounts,
              @Param("total") float total);

    @Query("SELECT o.issues FROM Order o WHERE o.issues LIKE CONCAT(\'%\', ?1, \'%\')")
    List<String> findByIssue(String issueId);

}
