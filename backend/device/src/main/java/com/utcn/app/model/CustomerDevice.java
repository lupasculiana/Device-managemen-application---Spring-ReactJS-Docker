package com.utcn.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDevice {
    @Id
    @GeneratedValue
    private Integer id;
    private Integer customerId;
    private Integer deviceId;
}
