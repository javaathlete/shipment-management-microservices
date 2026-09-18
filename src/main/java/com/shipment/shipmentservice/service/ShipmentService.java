package com.shipment.shipmentservice.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.shipment.shipmentservice.dto.CreateShipmentRequest;
import com.shipment.shipmentservice.dto.ShipmentResponse;
import com.shipment.shipmentservice.dto.UpdateShipmentRequest;
import com.shipment.shipmentservice.entity.Shipment;
import com.shipment.shipmentservice.entity.ShipmentStatus;
import com.shipment.shipmentservice.entity.ShipmentStatusHistory;
import com.shipment.shipmentservice.exception.InvalidShipmentStatusTransitionException;
import com.shipment.shipmentservice.exception.ShipmentCancletionException;
import com.shipment.shipmentservice.exception.ShipmentIdNotFoundException;
import com.shipment.shipmentservice.exception.ShipmentNotFoundException;
import com.shipment.shipmentservice.repository.ShipmentRepository;
import com.shipment.shipmentservice.repository.ShipmentStatusHistoryRepository;
import com.shipment.shipmentservice.specification.ShipmentSpecification;
import com.shipment.shipmentservice.util.ShipmentNumberTrackingGenerator;

import jakarta.transaction.Transactional;

@Service
public class ShipmentService {

	private final ShipmentRepository shipmentRepository;
	private final ShipmentNumberTrackingGenerator shipmentNumberTrackingGenerator;
	private final ShipmentStatusHistoryRepository historyRepository;

	public ShipmentService(ShipmentRepository shipmentRepository,
			ShipmentNumberTrackingGenerator shipmentNumberTrackingGenerator,
			ShipmentStatusHistoryRepository historyRepository) {
		this.shipmentRepository = shipmentRepository;
		this.shipmentNumberTrackingGenerator = shipmentNumberTrackingGenerator;
		this.historyRepository = historyRepository;
	}

	@Transactional
	public ShipmentResponse createShipment(CreateShipmentRequest request) {

		Shipment shipment = createShipmentEntites(request);
	    Shipment shipmentRecords = shipmentRepository.save(shipment);// save in shipment table
	    
	    ShipmentStatusHistory shipmentStatusHistory = saveShipmentStatusHistory(shipmentRecords);
	    
	   historyRepository.save(shipmentStatusHistory);// save in shipment History table

		return mapToResponse(shipmentRecords);

	}

	private ShipmentStatusHistory saveShipmentStatusHistory(Shipment shipmentRecords) {
		ShipmentStatusHistory shipmentStatusHistory = new ShipmentStatusHistory();
		
		shipmentStatusHistory.setShipment(shipmentRecords);
		shipmentStatusHistory.setOldStatus(null);
		shipmentStatusHistory.setNewStatus(ShipmentStatus.CREATED);
		shipmentStatusHistory.setChangedAt(LocalDateTime.now());
		return shipmentStatusHistory;
	}

	public ShipmentResponse getShipmentById(Long shipmentId) {
		Shipment shipment = shipmentRepository.findById(shipmentId)
				.orElseThrow(() -> new ShipmentIdNotFoundException(shipmentId));
		return mapToResponse(shipment);
	}

	@Transactional
	public List<ShipmentResponse> createBulkRecords(List<CreateShipmentRequest> bulkRequest) {

		List<Shipment> shipment = bulkRequest.stream().map(this::createBulkShipmentEntites).toList();

		List<Shipment> shipRecords = shipmentRepository.saveAll(shipment);

		return shipRecords.stream().map(this::mapToResponse).toList();
	}

	public List<ShipmentResponse> getAllShipmentRecords() {

		List<Shipment> shipmentList = shipmentRepository.findAll();
		return shipmentList.stream().map(this::mapToResponse).toList();
	}

	@Transactional
	public ShipmentResponse updateShipmentStatus(Long shipId, ShipmentStatus newStatus) {

		Shipment shipment = shipmentRepository.findById(shipId)
				.orElseThrow(() -> new ShipmentNotFoundException(shipId));

		ShipmentStatus existingShipmentStatus = shipment.getShipmentStatus();
		validateShipmentStatus(existingShipmentStatus, newStatus);

		shipment.setShipmentStatus(newStatus);
		shipment.setUpdatedAt(LocalDateTime.now());

		Shipment saveUpdatedStatusShipment = shipmentRepository.save(shipment);

		return mapToResponse(saveUpdatedStatusShipment);
	}

	@Transactional
	public ShipmentResponse cancleShipment(Long shipmentId) {

		Shipment shipment = shipmentRepository.findById(shipmentId)
				.orElseThrow(() -> new ShipmentNotFoundException(shipmentId));

		if (shipment.getShipmentStatus() != ShipmentStatus.CREATED) {
			throw new ShipmentCancletionException(shipment.getShipmentStatus());
		}

		shipment.setShipmentStatus(ShipmentStatus.CANCELLED);
		shipment.setUpdatedAt(LocalDateTime.now());
		Shipment shipmentCancled = shipmentRepository.save(shipment);

		return mapToResponse(shipmentCancled);
	}

	@Transactional
	public ShipmentResponse updateShipment(Long shipmentId, UpdateShipmentRequest request) {

		Shipment shipment = shipmentRepository.findById(shipmentId)
				.orElseThrow(() -> new ShipmentNotFoundException(shipmentId));
		shipment.setSenderName(request.getSenderName());
		shipment.setReceiverName(request.getReceiverName());
		shipment.setSenderPhone(request.getSenderPhone());
		shipment.setReceiverPhone(request.getReceiverPhone());
		shipment.setPickupAddress(request.getPickupAddress());
		shipment.setDeliveryAddress(request.getDeliveryAddress());
		shipment.setPackageWeight(request.getPackageWeight());
		shipment.setPackageDescription(request.getPackageDescription());

		shipment.setUpdatedAt(LocalDateTime.now());

		return mapToResponse(shipmentRepository.save(shipment));

	}

	public List<ShipmentResponse> searchShipmentByStatus(ShipmentStatus shipmentStatus)
			throws MethodArgumentTypeMismatchException {

		return shipmentRepository.findByShipmentStatus(shipmentStatus).stream().map(this::mapToResponse).toList();

	}

	public List<ShipmentResponse> findCustomerById(Long custId) {
		return shipmentRepository.findByCustomerId(custId).stream().map(this::mapToResponse).toList();
	}

	public List<ShipmentResponse> searchShipment(Long customerId, ShipmentStatus status, String trackingNo,
			LocalDateTime createdFromDate, LocalDateTime createdTo, String senderName) {

		List<Specification<Shipment>> specification = new ArrayList<>();

		if (customerId != null)
			specification.add(ShipmentSpecification.hasCustomerId(customerId));

		if (status != null)
			specification.add(ShipmentSpecification.hasStatus(status));

		if (trackingNo != null && !trackingNo.isBlank())
			specification.add(ShipmentSpecification.hasTrackingNumber(trackingNo));

		if (createdFromDate != null)
			specification.add(ShipmentSpecification.createdFromGreaterThenOrEqual(createdFromDate));

		if (createdTo != null)
			specification.add(ShipmentSpecification.createdToLessThenOrEqualTo(createdTo));

		if (senderName != null && !senderName.isBlank())
			specification.add(ShipmentSpecification.searchLikeUserName(senderName));

		Specification<Shipment> specifications = Specification.allOf(specification);
		return shipmentRepository.findAll(specifications).stream().map(this::mapToResponse).toList();

	}

	public Page<ShipmentResponse> getAllShipmentPageWise(int page, int size) {

		Pageable pageable = PageRequest.of(page, size);
		Page<Shipment> pageResponse = shipmentRepository.findAll(pageable);
		return pageResponse.map(this::mapToResponse);
	}

	private void validateShipmentStatus(ShipmentStatus existingShipmentStatus, ShipmentStatus newStatus) {

		switch (existingShipmentStatus) {

		case CREATED:

			if (newStatus != ShipmentStatus.ROUTE_ASSIGNED && newStatus != ShipmentStatus.CANCELLED) {

				throw new InvalidShipmentStatusTransitionException(existingShipmentStatus, newStatus);
			}

			break;

		case ROUTE_ASSIGNED:

			if (newStatus != ShipmentStatus.PICKED_UP) {

				throw new InvalidShipmentStatusTransitionException(existingShipmentStatus, newStatus);
			}

			break;

		case PICKED_UP:

			if (newStatus != ShipmentStatus.IN_TRANSIT) {

				throw new InvalidShipmentStatusTransitionException(existingShipmentStatus, newStatus);
			}

			break;

		case IN_TRANSIT:

			if (newStatus != ShipmentStatus.OUT_FOR_DELIVERY) {

				throw new InvalidShipmentStatusTransitionException(existingShipmentStatus, newStatus);
			}

			break;

		case OUT_FOR_DELIVERY:

			if (newStatus != ShipmentStatus.DELIVERED) {

				throw new InvalidShipmentStatusTransitionException(existingShipmentStatus, newStatus);
			}

			break;

		case DELIVERED:
		case CANCELLED:

			throw new InvalidShipmentStatusTransitionException(existingShipmentStatus, newStatus);

		}
	}

	private ShipmentResponse mapToResponse(Shipment savedShipment) {
		ShipmentResponse response = new ShipmentResponse();

		response.setShipmentId(savedShipment.getShipmentId());
		response.setCustomerId(savedShipment.getCustomerId());
		response.setTrackingNumber(savedShipment.getTrackingNumber());
		response.setSenderName(savedShipment.getSenderName());
		response.setReceiverName(savedShipment.getReceiverName());
		response.setPickupAddress(savedShipment.getPickupAddress());
		response.setDeliveryAddress(savedShipment.getDeliveryAddress());
		response.setPackageDescription(savedShipment.getPackageDescription());
		response.setPackageWeight(savedShipment.getPackageWeight());
		response.setShipmentStatus(savedShipment.getShipmentStatus());
		response.setCreatedAt(savedShipment.getCreatedAt());
		response.setUpdatedAt(savedShipment.getUpdatedAt());

		return response;
	}

	private Shipment createBulkShipmentEntites(CreateShipmentRequest request) {

		Shipment shipment = new Shipment();

		shipment.setCustomerId(request.getCustomerId());
		shipment.setSenderName(request.getSenderName());
		shipment.setSenderPhone(request.getSenderPhone());
		shipment.setReceiverName(request.getReceiverName());
		shipment.setReceiverPhone(request.getReceiverPhone());
		shipment.setPickupAddress(request.getPickupAddress());
		shipment.setDeliveryAddress(request.getDeliveryAddress());
		shipment.setPackageWeight(request.getPackageWeight());
		shipment.setPackageDescription(request.getPackageDescription());

		// added 3 atributes in shipment entity tracking id,status , created_date and
		// updated_date
		// these 4 attributes is not comming from request b'z business is creating these
		// values not client/customer

		shipment.setShipmentStatus(ShipmentStatus.CREATED);
		shipment.setCreatedAt(LocalDateTime.now());
		shipment.setUpdatedAt(LocalDateTime.now());

		shipment.setTrackingNumber(shipmentNumberTrackingGenerator.generateTrackingNumber());

		return shipment;
	}

	public List<ShipmentResponse> dateTimeLike(String yyyyddmm) {

		List<Specification<Shipment>> searchList = new ArrayList<>();

		if (yyyyddmm != null)
			searchList.add(ShipmentSpecification.searchLikeOnlyDateInyyyyMMddFormat(yyyyddmm));

		Specification<Shipment> shipSpecifications = Specification.allOf(searchList);
		return shipmentRepository.findAll(shipSpecifications).stream().map(this::mapToResponse).toList();
	}

	private Shipment createShipmentEntites(CreateShipmentRequest request) {
		Shipment shipment = new Shipment();

		shipment.setCustomerId(request.getCustomerId());
		shipment.setTrackingNumber(shipmentNumberTrackingGenerator.generateTrackingNumber());
		shipment.setSenderName(request.getSenderName());
		shipment.setSenderPhone(request.getSenderPhone());
		shipment.setReceiverName(request.getReceiverName());
		shipment.setReceiverPhone(request.getReceiverPhone());
		shipment.setPickupAddress(request.getPickupAddress());
		shipment.setDeliveryAddress(request.getDeliveryAddress());
		shipment.setPackageWeight(request.getPackageWeight());
		shipment.setPackageDescription(request.getPackageDescription());

		shipment.setShipmentStatus(ShipmentStatus.CREATED);

		LocalDateTime now = LocalDateTime.now();
		shipment.setCreatedAt(now);
		shipment.setUpdatedAt(now);
		return shipment;

	}

}
