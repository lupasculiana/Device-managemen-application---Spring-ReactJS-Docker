package com.example.messagem.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceDTO {
    private Integer id;
    private Integer maximumHourlyEnergyConsumption;
}
