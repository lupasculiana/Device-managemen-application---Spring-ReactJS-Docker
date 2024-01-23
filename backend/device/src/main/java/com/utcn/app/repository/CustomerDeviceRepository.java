package com.utcn.app.repository;

import com.utcn.app.model.CustomerDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerDeviceRepository extends JpaRepository<CustomerDevice, Integer> {
    Optional<CustomerDevice> findCustomerDeviceByDeviceId(Integer deviceId);

    Optional<List<CustomerDevice>> findCustomerDevicesByCustomerId(Integer customerId);

    Boolean existsByDeviceId(Integer deviceId);
}
