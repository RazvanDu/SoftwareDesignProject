package com.Project.CatalogService.Database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeriesRepository extends JpaRepository<Series, Integer> {

    @Query(value = "select * from Series where title = ?1", nativeQuery = true)
    List<Series> findByTitle(String title);

    @Query("select u from Series u where u.publisher.id = ?1")
    List<Series> findByPublisher(int id);

}
