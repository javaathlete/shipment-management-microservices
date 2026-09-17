package com.shipment.shipmentservice.specification;

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



/* This is equivalant to 
     
      return (Root<Shipment> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) ->  criteriaBuilder.equal(
                root.get("customerId"), customerId );

*/

	
	
	public static Specification<Shipment> hasTrackingNumber(String trackingNo) {
		return (root,query,criteriaBuilder)->criteriaBuilder.equal(root.get("trackingNumber"), trackingNo);
	}
}