package com.parkee.parkee.tickets.controller;


import com.parkee.parkee.response.Response;
import com.parkee.parkee.tickets.DTO.RequestTicketDTO;
import com.parkee.parkee.tickets.DTO.RequestTicketDTOCheckout;
import com.parkee.parkee.tickets.DTO.ResponseTicketCheckoutDTO;
import com.parkee.parkee.tickets.DTO.ResponseTicketDTO;
import com.parkee.parkee.tickets.entity.Tickets;
import com.parkee.parkee.tickets.services.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {
    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping("")
    public ResponseEntity<Response<ResponseTicketDTO>> createNewTicket(@RequestBody RequestTicketDTO requestTicketDto) {
        ResponseTicketDTO responseTicketDTO = ticketService.createNewTicket(requestTicketDto);
        return Response.successfulResponse(HttpStatus.CREATED.value(),"Ticket has been successfully created",responseTicketDTO);
    }

    @PostMapping("/check")
    public ResponseEntity<Response<ResponseTicketCheckoutDTO>> createNewTicket(MultipartFile image) {
        return Response.successfulResponse(HttpStatus.OK.value(),"Ticket data has been sucessfully processed",ticketService.getTicket(image));
    }

    @PatchMapping("")
    public ResponseEntity<Response<Tickets>> updateTicketData(@RequestBody RequestTicketDTOCheckout requestTicketDTOCheckout) {
        return Response.successfulResponse(HttpStatus.OK.value(),"Ticket data has been sucessfully updated",ticketService.checkoutTicket(requestTicketDTOCheckout));
    }

}
