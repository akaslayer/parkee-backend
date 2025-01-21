package com.parkee.parkee.tickets.services;


import com.parkee.parkee.OCR.entity.OCRData;
import com.parkee.parkee.OCR.services.OCRService;
import com.parkee.parkee.exceptions.DataConstraintException;
import com.parkee.parkee.tickets.DTO.RequestTicketDTO;
import com.parkee.parkee.tickets.DTO.RequestTicketDTOCheckout;
import com.parkee.parkee.tickets.DTO.ResponseTicketCheckoutDTO;
import com.parkee.parkee.tickets.DTO.ResponseTicketDTO;
import com.parkee.parkee.tickets.entity.TicketStatus;
import com.parkee.parkee.tickets.entity.Tickets;
import com.parkee.parkee.tickets.repository.TicketRepository;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final OCRService ocrService;

    public TicketServiceImpl(TicketRepository ticketRepository, OCRService ocrService) {
        this.ticketRepository = ticketRepository;
        this.ocrService = ocrService;
    }

    @Override
    public ResponseTicketDTO createNewTicket(RequestTicketDTO requestTicketDTO) {
        Optional<Tickets> checkTicket = ticketRepository.findFirstByStatusAndNomorPlat(TicketStatus.valueOf("PENDING"),requestTicketDTO.getNomorPlat());
        if(checkTicket.isPresent()){
            throw new DataConstraintException("Last Checkout ticket haven't d one yet");
        }
        Tickets ticket = new Tickets();
        UUID uuid = UUID.randomUUID();
        String substr = uuid.toString().substring(0,12);
        ticket.setNomorPlat(requestTicketDTO .getNomorPlat());
        ticket.setJenisKendaraan(requestTicketDTO.getJenisKendaraan());
        ticket.setNomorParkingSlip(substr);
        TicketStatus status = TicketStatus.valueOf("PENDING");
        ticket.setStatus(status);
        Tickets newTicket = ticketRepository.save(ticket);
        ResponseTicketDTO response = new ResponseTicketDTO();
        response.setId(newTicket.getId());
        response.setNomorPlat(newTicket.getNomorPlat());
        response.setNomorParkingSlip(newTicket.getNomorParkingSlip());
        response.setTanggalMasuk(newTicket.getTanggalMasuk());
        response.setJenisKendaraan(String.valueOf(newTicket.getJenisKendaraan()));
        return response;
    }

    @Override
    public ResponseTicketCheckoutDTO getTicket(MultipartFile image) {
        try {
            OCRData ticketData = ocrService.extractTicketInfo(image);
            Optional<Tickets> tickets = ticketRepository.findByNomorParkingSlip(ticketData.getNomorTiket());
            if (tickets.isEmpty()) {
                throw new DataConstraintException("Ticket with nomor plat " + ticketData.getNomorTiket()+ " not found");
            }
            ResponseTicketCheckoutDTO responseTicketCheckoutDTO = new ResponseTicketCheckoutDTO();
            Instant currentTime = Instant.now();
            Tickets ticket = tickets.get();
            responseTicketCheckoutDTO.setId(ticket.getId());
            responseTicketCheckoutDTO.setJenisKendaraan(ticket.getJenisKendaraan());
            responseTicketCheckoutDTO.setTanggalMasuk(ticket.getTanggalMasuk());
            responseTicketCheckoutDTO.setTanggalKeluar(currentTime);
            responseTicketCheckoutDTO.setNomorPlat(ticket.getNomorPlat());
            responseTicketCheckoutDTO.setNomorParkingSlip(ticket.getNomorParkingSlip());
            Duration duration = Duration.between(ticket.getTanggalMasuk(), currentTime);
            long totalDays = duration.toDays();
            long totalMinutes = duration.toMinutes();
            long remainingHours = (totalMinutes / 60) % 24;
            long remainingMinutes = totalMinutes % 60;
            long totalHoursRoundedUp = (totalMinutes + 59) / 60;
            double totalPrice = totalHoursRoundedUp * 3000.0;
            StringBuilder durationStr = new StringBuilder();
            durationStr.append(totalDays).append(" days ");
            durationStr.append(remainingHours).append(" hours ");
            durationStr.append(remainingMinutes).append(" minutes");
            responseTicketCheckoutDTO.setTotalHarga(totalPrice);
            responseTicketCheckoutDTO.setTotalWaktu(durationStr.toString());
            return responseTicketCheckoutDTO;
        } catch (IOException | TesseractException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Tickets checkoutTicket(RequestTicketDTOCheckout requestTicketDTOCheckout) {
        Optional<Tickets> tickets = ticketRepository.findByNomorParkingSlip(requestTicketDTOCheckout.getNomorTiket());
        if (tickets.isEmpty()) {
            throw new DataConstraintException("Ticket with nomor plat " + requestTicketDTOCheckout.getNomorTiket() + " not found");
        }
        Tickets updateTicket = tickets.get();
        updateTicket.setTanggalKeluar(requestTicketDTOCheckout.getTanggalKeluar());
        TicketStatus status = TicketStatus.valueOf("COMPLETE");
        updateTicket.setStatus(status);
        updateTicket.setMetodePembayaran(requestTicketDTOCheckout.getMetodePembayaran());
        updateTicket.setTotalBayar(requestTicketDTOCheckout.getTotalHarga());
        ticketRepository.save(updateTicket);
        return updateTicket;
    }
}
