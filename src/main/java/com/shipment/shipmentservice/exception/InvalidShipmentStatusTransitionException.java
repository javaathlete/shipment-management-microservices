package com.shipment.shipmentservice.exception;

import com.shipment.shipmentservice.entity.ShipmentStatus;

public class InvalidShipmentStatusTransitionException extends RuntimeException {

	public InvalidShipmentStatusTransitionException(ShipmentStatus currentStatus, ShipmentStatus newStatus) {

		super("Invalid Shipment status transition from " + currentStatus + "  TO  " + newStatus);
	}

}
