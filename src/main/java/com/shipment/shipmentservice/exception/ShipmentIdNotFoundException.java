package com.shipment.shipmentservice.exception;

public class ShipmentIdNotFoundException extends RuntimeException {

	public ShipmentIdNotFoundException(Long shipmentId) {
		super(shipmentId+ " This Shipment id not found...Try other id");
	}
	

}
