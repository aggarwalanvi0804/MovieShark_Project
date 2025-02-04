package com.project.movieshark.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.movieshark.domain.Theater;


@Repository
public interface TheaterRepository extends JpaRepository<Theater, Long> {

}