package com.shipment.shipmentservice.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shipment.shipmentservice.dto.CreateShipmentRequest;
import com.shipment.shipmentservice.dto.ShipmentResponse;
import com.shipment.shipmentservice.dto.UpdateShipmentRequest;
import com.shipment.shipmentservice.dto.UpdateShipmentStatusRequest;
import com.shipment.shipmentservice.entity.ShipmentStatus;
import com.shipment.shipmentservice.service.ShipmentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/shipments")
public class ShipmentController {

	private final ShipmentService shipmentService;

	public ShipmentController(ShipmentService shipmentService) {
		this.shipmentService = shipmentService;
	}

	@PostMapping
	public ResponseEntity<ShipmentResponse> createShipment(
			@Valid @RequestBody CreateShipmentRequest createShipmentRequest) {

		ShipmentResponse shipmentResponse = shipmentService.createShipment(createShipmentRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(shipmentResponse);
	}

	@GetMapping("/{shipmentId}")
	public ResponseEntity<ShipmentResponse> getShipmentBySpecifiedId(@PathVariable Long shipmentId) {

		ShipmentResponse response = shipmentService.getShipmentById(shipmentId);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/savebulkrecords")
	public ResponseEntity<List<ShipmentResponse>> createBulkResources(
			@RequestBody List<CreateShipmentRequest> bulkRequest) {

		List<ShipmentResponse> records = shipmentService.createBulkRecords(bulkRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(records);
	}

	@GetMapping("/getAll")
	public ResponseEntity<List<ShipmentResponse>> getAllRecords() {
		List<ShipmentResponse> shipmentResponse = shipmentService.getAllShipmentRecords();
		return ResponseEntity.status(HttpStatus.FOUND).body(shipmentResponse);
	}

	@PatchMapping("/{shipmentId}/status")
	public ResponseEntity<ShipmentResponse> updateShipmentStatus(@PathVariable Long shipmentId,
			@Valid @RequestBody UpdateShipmentStatusRequest updateStatus) {

		ShipmentResponse shipmentResponse = shipmentService.updateShipmentStatus(shipmentId, updateStatus.getStatus());
		return ResponseEntity.ok(shipmentResponse);

	}

	@PatchMapping("/{shipmentId}/cancle")
	public ResponseEntity<ShipmentResponse> cancleShipment(@PathVariable Long shipmentId) {
		ShipmentResponse shipmentResponse = shipmentService.cancleShipment(shipmentId);
		return ResponseEntity.ok(shipmentResponse);
	}

	@PutMapping("/{shipmentId}")
	public ResponseEntity<ShipmentResponse> updateShipment(@PathVariable Long shipmentId,
			@Valid @RequestBody UpdateShipmentRequest updateShipmentRequest) {

		ShipmentResponse response = shipmentService.updateShipment(shipmentId, updateShipmentRequest);
		return ResponseEntity.ok(response);
	}

	@GetMapping(params = "status")
	public ResponseEntity<List<ShipmentResponse>> getShipmentsByStatus(@RequestParam ShipmentStatus status) {

		List<ShipmentResponse> response = shipmentService.searchShipmentByStatus(status);
		return ResponseEntity.ok(response);
	}

	@GetMapping(params = "customerId")
	public ResponseEntity<List<ShipmentResponse>> getShipmentsByCustomerId(@RequestParam Long customerId) {

		List<ShipmentResponse> shipments = shipmentService.findCustomerById(customerId);
		return ResponseEntity.ok(shipments);
	}

	@GetMapping("/search")
	public ResponseEntity<List<ShipmentResponse>> searchShipment(@RequestParam(required = false) Long customerId,
			@RequestParam(required = false) ShipmentStatus status, @RequestParam(required = false) String trackingNo,
			@RequestParam(required = false) LocalDateTime createdFrom,
			@RequestParam(required = false) LocalDateTime createdTo,
			@RequestParam(required = false) String senderName) {

		List<ShipmentResponse> response = shipmentService.searchShipment(customerId, status, trackingNo, createdFrom,
				createdTo, senderName);
		return ResponseEntity.ok().body(response);
	}

	@GetMapping("/searchLike")
	public ResponseEntity<List<ShipmentResponse>> searchShipmentLike(
			@RequestParam(required = false) String datePattern) {
		List<ShipmentResponse> responses = shipmentService.dateTimeLike(datePattern);
		return ResponseEntity.ok().body(responses);
	}

	@GetMapping("/pageable")
	public ResponseEntity<Page<ShipmentResponse>> getAllShipmentsPageByPage(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size)
	{
		Page<ShipmentResponse> respoPage = shipmentService.getAllShipmentPageWise(page, size);
		return ResponseEntity.ok().body(respoPage);

	}
}
