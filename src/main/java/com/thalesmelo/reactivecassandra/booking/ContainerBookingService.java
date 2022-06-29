package com.thalesmelo.reactivecassandra.booking;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.thalesmelo.reactivecassandra.booking.api.ContainerBookingReferenceDto;
import com.thalesmelo.reactivecassandra.config.ContainerBookingDto;
import com.thalesmelo.reactivecassandra.db.EntityReferenceRepository;
import com.thalesmelo.reactivecassandra.integrations.InvalidExternalServiceResponseException;

import reactor.core.publisher.Mono;

@Service
public class ContainerBookingService {

	private static final String BOOKING_REFERENCE_NAME = ContainerBookingReference.class.getSimpleName();

	private static final String BOOKING_REFERENCE_FORMAT = "%019d";

	@Autowired
	private ContainerBookingRepository repository;

	@Autowired
	private ContainerBookingReferenceRepository bookingReferenceRepository;

	@Autowired
	private EntityReferenceRepository entityReferenceRepository;

	@Autowired
	private RestTemplate externalService;

	@Value("${external.service.check.container.availabilty.url}")
	private String externalServiceUrl;

	public Mono<ContainerBookingReferenceDto> createBooking(ContainerBookingDto dto) {
		BookingModelMapper mapper = new BookingModelMapper();

		ContainerBooking containerBooking = mapper.map(validate(dto));
		return repository.save(containerBooking)//
				.log()//
				.flatMap(entity -> getNewReferenceBooking(entity))//
				.flatMap(reference -> Mono.just(mapper.map(reference)));
	}

	private Mono<ContainerBookingReference> getNewReferenceBooking(ContainerBooking entity) {
		return  getNextReferenceNumber()//
				.flatMap( nextReferenceNumber -> Mono.just(createNewBookingReference(entity, nextReferenceNumber)))//
				.flatMap( bookingReference -> bookingReferenceRepository.save(bookingReference));
	}

	private ContainerBookingReference createNewBookingReference(ContainerBooking entity, Long nextReferenceNumber) {
		// It formats the number to have 19 digits adding leading zeros if necessary
		String formattedReferenceNumber = String.format(BOOKING_REFERENCE_FORMAT, nextReferenceNumber);
		return new ContainerBookingReference( formattedReferenceNumber, entity.getId());
	}

	// In case of Race condition, we confirm the allocation of the next reference
	// available using an update against the key table.
	private Mono<Long> getNextReferenceNumber() {
		return entityReferenceRepository.findByEntityName(BOOKING_REFERENCE_NAME)//
				.log()
				.flatMap(ref -> entityReferenceRepository.updateReference(BOOKING_REFERENCE_NAME, ref.getNext()))//
				.log()
				.retry(5)// 5 retries to get a new ID
				.flatMap(ref -> Mono.just(ref.getCurrent()));
	}

	public Mono<AvailableBookingDto> isAvailable(ContainerBookingDto booking) {
		try {
			int containersAvailableInYard = getContainersAvailableInYard(booking); 
			return Mono.just(new AvailableBookingDto(containersAvailableInYard > 0));
		} catch (Exception e) {
			return Mono.error(() -> e);
		}
	}

	private Integer getContainersAvailableInYard(ContainerBookingDto booking)
			throws URISyntaxException, InvalidExternalServiceResponseException {
		validate(booking);
		RequestEntity checkAvailabilityOfContainerForYardRequest = RequestEntity.post(new URI(externalServiceUrl))
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).body(booking);
		ResponseEntity<Integer> response = externalService.postForEntity(new URI(externalServiceUrl),
				checkAvailabilityOfContainerForYardRequest, Integer.class);

		if (HttpStatus.OK == response.getStatusCode()) {
			return response.getBody();
		} else {
			throw new InvalidExternalServiceResponseException("Unable to get external server information.");
		}
	}

	/**
	 * This could have been done in the entity on the eDAO layer using the
	 * annotations, it could have been also done in the rest layer. But here is a
	 * good place to ensure no invalid data goes through the business layer
	 */
	private ContainerBookingDto validate(ContainerBookingDto dto) {
		Objects.requireNonNull(dto);
		Objects.requireNonNull(dto.getContainerType());
		// Origin
		if (dto.getOrigin().length() < 5 || dto.getOrigin().length() > 20) {
			throw new IllegalArgumentException("Origin length should be between 5 and 20");
		}
		// Destination
		if (dto.getDestination().length() < 5 || dto.getDestination().length() > 20) {
			throw new IllegalArgumentException("Destination length should be between 5 and 20");
		}
		// Container size
		if (dto.getContainerSize() != 20 && dto.getContainerSize() != 40) {
			throw new IllegalArgumentException("Container size should be 20 or 40");
		}
		// QUantity
		if (dto.getQuantity() < 1 || dto.getContainerSize() > 100) {
			throw new IllegalArgumentException("Container size should be between 1 and 100");
		}
		return dto;
	}

}