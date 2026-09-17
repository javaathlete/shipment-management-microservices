package com.shipment.shipmentservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class UpdateShipmentRequest {
	@NotBlank(message = "Sender Name is Required")
	private String senderName;
	@NotBlank(message = "Sender Phone is Required")
	private String senderPhone;
	@NotBlank(message = "Receiver Name is Required")
	private String receiverName;
	@NotBlank(message = "Receiver Phone is Required")
	private String receiverPhone;
	@NotBlank(message = "Pickup Address is Required")
	private String pickupAddress;
	@NotBlank(message = "Delivery Address is Required")
	private String deliveryAddress;
	@NotNull(message = "Weight of package should not be 0 or (-)ve ")
	@Positive
	private Double packageWeight;
	private String packageDescription;

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getSenderPhone() {
		return senderPhone;
	}

	public void setSenderPhone(String senderPhone) {
		this.senderPhone = senderPhone;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getReceiverPhone() {
		return receiverPhone;
	}

	public void setReceiverPhone(String receiverPhone) {
		this.receiverPhone = receiverPhone;
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

}
