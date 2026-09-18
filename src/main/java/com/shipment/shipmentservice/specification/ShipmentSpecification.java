package com.shipment.shipmentservice.specification;

import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;

import com.shipment.shipmentservice.entity.Shipment;
import com.shipment.shipmentservice.entity.ShipmentStatus;

public class ShipmentSpecification {
	
	public static Specification<Shipment> hasCustomerId(Long customerId) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("customerId"),customerId);
	}
	
	public static Specification<Shipment> hasStatus(ShipmentStatus status) {
		return (root,query,criteriaBuilder)->criteriaBuilder.equal(root.get("shipmentStatus"), status);
	}
	
	public static Specification<Shipment> hasTrackingNumber(String trackingNo) {
		return (root,query,criteriaBuilder)->criteriaBuilder.equal(root.get("trackingNumber"), trackingNo);
	}
	
	public static Specification<Shipment> createdFromGreaterThenOrEqual(LocalDateTime createdFrom) {
		return (root,query,criteriaBuilder)->criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), createdFrom);
	}
	
	public static Specification<Shipment> createdToLessThenOrEqualTo(LocalDateTime createdTo) {
		return (root,query,criteriaBuilder)->criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), createdTo);
	}
	
	public static Specification<Shipment>searchLikeOnlyDateInyyyyMMddFormat(String trackingNo) {
		return (root,query,criteriaBuilder)->criteriaBuilder.like(root.get("trackingNumber"), "%"+trackingNo+"%");
	}
	public static Specification<Shipment>searchLikeUserName(String uName) {
		return (root,query,criteriaBuilder)->criteriaBuilder.like(criteriaBuilder.lower(root.get("senderName")),"%" +uName.toLowerCase()+"%");
	}
}






















