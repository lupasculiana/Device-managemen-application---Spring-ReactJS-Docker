package com.example.messagem.repository;

import com.example.messagem.model.Device;
import com.example.messagem.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Integer> {
}