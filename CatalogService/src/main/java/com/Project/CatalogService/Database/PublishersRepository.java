package com.Project.CatalogService.Database;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface PublishersRepository extends JpaRepository<Publisher, Integer> {

}
