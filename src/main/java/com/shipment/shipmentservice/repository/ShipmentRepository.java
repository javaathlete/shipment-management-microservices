package com.shipment.shipmentservice.repository;

import java.awt.print.Pageable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.shipment.shipmentservice.entity.Shipment;
import com.shipment.shipmentservice.entity.ShipmentStatus;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long>, JpaSpecificationExecutor<Shipment> {
	
	List<Shipment> findByShipmentStatus(ShipmentStatus shipmentStatus);
	
	List<Shipment> findByCustomerId(Long customerId);


}
