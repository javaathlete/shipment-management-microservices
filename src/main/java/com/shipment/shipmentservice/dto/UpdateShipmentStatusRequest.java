package com.shipment.shipmentservice.dto;

import com.shipment.shipmentservice.entity.ShipmentStatus;

import jakarta.validation.constraints.NotNull;

public class UpdateShipmentStatusRequest {

	@NotNull(message = "Shipment Status is required")
	private ShipmentStatus status;

	public ShipmentStatus getStatus() {
		return status;
	}

	public void setStatus(ShipmentStatus status) {
		this.status = status;
	}

}
