package com.shipment.shipmentservice.exception;

import com.shipment.shipmentservice.entity.ShipmentStatus;

public class ShipmentCancletionException extends RuntimeException {

	public ShipmentCancletionException(ShipmentStatus shipmentStatus) {
		super("Shipment cannot be cancled when status is: "+shipmentStatus);
	}
	
}
