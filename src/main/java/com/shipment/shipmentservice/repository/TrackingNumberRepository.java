package com.shipment.shipmentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shipment.shipmentservice.entity.Shipment;

@Repository
public interface TrackingNumberRepository extends JpaRepository<Shipment, Long> {
	
	@Query(value = "SELECT TRACKING_NUMBER_SEQ.NEXTVAL FROM DUAL", nativeQuery = true)
    Long getNextTrackingNumberSequence();
}

// Why native SQL?
/*

Because:

TRACKING_NUMBER_SEQ.NEXTVAL is an Oracle-specific database sequence operation.

JPQL doesn't provide Oracle's NEXTVAL syntax directly.

So this is an appropriate place for a small native query.






*/