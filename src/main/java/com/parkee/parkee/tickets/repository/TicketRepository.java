package com.parkee.parkee.tickets.repository;

import com.parkee.parkee.tickets.entity.TicketStatus;
import com.parkee.parkee.tickets.entity.Tickets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface TicketRepository extends JpaRepository<Tickets, Long> {
    Optional<Tickets> findFirstByStatusAndNomorPlat(TicketStatus status, String nomorPlat);

    Optional<Tickets> findByNomorPlat(String nomorPlat);

    Optional<Tickets> findByNomorParkingSlip(String nomorParkingSlip);
}
