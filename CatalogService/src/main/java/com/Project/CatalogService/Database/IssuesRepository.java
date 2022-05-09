package com.Project.CatalogService.Database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssuesRepository extends JpaRepository<Issue, Integer> {

    @Query(value = "SELECT * FROM issues WHERE Writer = ?1", nativeQuery = true)
    List<Issue> findByWriter(int id);

    @Query(value = "SELECT * FROM issues WHERE Series = ?1", nativeQuery = true)
    List<Issue> findBySeries(int id);

}
