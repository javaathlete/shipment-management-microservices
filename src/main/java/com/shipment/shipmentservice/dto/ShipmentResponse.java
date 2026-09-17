package com.shipment.shipmentservice.dto;

import java.time.LocalDateTime;

import com.shipment.shipmentservice.entity.ShipmentStatus;

public class ShipmentResponse {
	private Long shipmentId;
	private Long customerId;
	private String trackingNumber;

	private String senderName;
	private String receiverName;

	private String pickupAddress;
	private String deliveryAddress;

	private Double packageWeight;
	private String packageDescription;

	private ShipmentStatus shipmentStatus;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public Long getShipmentId() {
		return shipmentId;
	}

	public void setShipmentId(Long shipmentId) {
		this.shipmentId = shipmentId;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getTrackingNumber() {
		return trackingNumber;
	}

	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}


	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getPickupAddress() {
		return pickupAddress;
	}

	public void setPickupAddress(String pickupAddress) {
		this.pickupAddress = pickupAddress;
	}

	public String getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public Double getPackageWeight() {
		return packageWeight;
	}

	public void setPackageWeight(Double packageWeight) {
		this.packageWeight = packageWeight;
	}

	public String getPackageDescription() {
		return packageDescription;
	}

	public void setPackageDescription(String packageDescription) {
		this.packageDescription = packageDescription;
	}

	public ShipmentStatus getShipmentStatus() {
		return shipmentStatus;
	}

	public void setShipmentStatus(ShipmentStatus shipmentStatus) {
		this.shipmentStatus = shipmentStatus;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	@Override
	public String toString() {
		return "ShipmentResponse [shipmentId=" + shipmentId + ", customerId=" + customerId + ", trackingNumber="
				+ trackingNumber + ", senderName=" + senderName + ", receiverName=" + receiverName + ", pickupAddress="
				+ pickupAddress + ", deliveryAddress=" + deliveryAddress + ", packageWeight=" + packageWeight
				+ ", packageDescription=" + packageDescription + ", shipmentStatus=" + shipmentStatus + ", createdAt="
				+ createdAt + ", updatedAt=" + updatedAt + "]";
	}

	
}
