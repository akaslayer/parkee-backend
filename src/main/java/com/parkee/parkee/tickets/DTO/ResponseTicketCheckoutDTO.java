package com.parkee.parkee.tickets.DTO;

import com.parkee.parkee.vehicle.entity.VehicleType;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;


@Setter
@Getter
public class ResponseTicketCheckoutDTO {
    private Long id;
    private VehicleType jenisKendaraan;
    private String nomorPlat;
    private String nomorParkingSlip;
    private Instant tanggalMasuk;
    private Instant tanggalKeluar;
    private Double totalHarga;
    private String totalWaktu;

}
