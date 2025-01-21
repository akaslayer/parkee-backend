package com.parkee.parkee.tickets.entity;

import com.parkee.parkee.vehicle.entity.VehicleType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.time.LocalDateTime;


@Entity
@Setter
@Getter
@Table(name = "tickets")
public class Tickets {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "jenis_kendaraan", nullable = false)
    private VehicleType jenisKendaraan;

    @Column(name = "nomor_plat", nullable = false)
    private String nomorPlat;

    @Column(name = "total_bayar")
    private Double totalBayar;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "tanggal_masuk", nullable = false)
    private Instant tanggalMasuk;

    @Column(name = "tanggal_keluar")
    private Instant tanggalKeluar;

    @Column(name = "metode_pembayaran")
    private String metodePembayaran;

    @Column(name = "nomor_parking_slip", nullable = false, unique = true)
    private String nomorParkingSlip;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TicketStatus status;


    @PrePersist
    protected void onCreate(){
        tanggalMasuk = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        tanggalKeluar = Instant.now();
    }
}
