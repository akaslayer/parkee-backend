package com.parkee.parkee.tickets.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;


@Getter
@Setter
public class ResponseTicketDTO {
    private Long id;
    private String jenisKendaraan;
    private String nomorPlat;
    private String nomorParkingSlip;
    private Instant tanggalMasuk;
}
