package com.shipment.shipmentservice.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

import com.shipment.shipmentservice.repository.TrackingNumberRepository;

@Component
public class ShipmentNumberTrackingGenerator {

	private TrackingNumberRepository numberRepository;

	public ShipmentNumberTrackingGenerator(TrackingNumberRepository numberRepository) {
		this.numberRepository = numberRepository;
	}

	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

	public String generateTrackingNumber() {

		String date = LocalDate.now().format(DATE_FORMATTER);
		long sequenceNumber = numberRepository.getNextTrackingNumberSequence();

		return String.format("SHP%s%06d", date, sequenceNumber);
	}
	
}
