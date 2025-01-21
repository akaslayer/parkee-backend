package com.parkee.parkee.tickets.services;

import com.parkee.parkee.tickets.DTO.RequestTicketDTO;
import com.parkee.parkee.tickets.DTO.RequestTicketDTOCheckout;
import com.parkee.parkee.tickets.DTO.ResponseTicketCheckoutDTO;
import com.parkee.parkee.tickets.DTO.ResponseTicketDTO;
import com.parkee.parkee.tickets.entity.Tickets;
import org.springframework.web.multipart.MultipartFile;

public interface TicketService {
    ResponseTicketDTO createNewTicket(RequestTicketDTO requestTicketDTO);
    ResponseTicketCheckoutDTO getTicket(MultipartFile image);
    Tickets checkoutTicket(RequestTicketDTOCheckout requestTicketDTOCheckout);
}
