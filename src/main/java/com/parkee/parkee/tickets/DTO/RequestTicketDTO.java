package com.parkee.parkee.tickets.DTO;


import com.parkee.parkee.vehicle.entity.VehicleType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class RequestTicketDTO {
    private String nomorPlat;
    private VehicleType jenisKendaraan;
}
