package com.parkee.parkee.tickets.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class RequestTicketDTOCheckout {
    private String nomorTiket;
    private Double totalHarga;
    private Instant tanggalKeluar;
    private String metodePembayaran;
}
