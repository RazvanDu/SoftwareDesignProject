package com.Project.CatalogService.Database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WritersRepository extends JpaRepository<Writer, Integer> {

    @Query(value = "SELECT * FROM Writers WHERE name = ?1", nativeQuery = true)
    List<Writer> findByName(String name);

}
