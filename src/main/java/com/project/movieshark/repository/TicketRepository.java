package com.project.movieshark.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.movieshark.domain.Ticket;


@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long>{

}