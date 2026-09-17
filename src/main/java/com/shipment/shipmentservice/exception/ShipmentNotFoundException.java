package com.shipment.shipmentservice.exception;

public class ShipmentNotFoundException extends RuntimeException {

	public ShipmentNotFoundException(Long shipmentId) {
		
		super("Shipment Id"+ shipmentId+" Not Found in record....");
	}

}
