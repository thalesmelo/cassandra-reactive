package com.thalesmelo.reactivecassandra.booking;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.thalesmelo.reactivecassandra.booking.pi.ContainerBookingDto;
import com.thalesmelo.reactivecassandra.booking.pi.ContainerBookingReferenceDto;
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

	@Autowired
	private ModelMapper mapper;

	@Value("${external.service.check.container.availabilty.url}")
	private String externalServiceUrl;

	public Mono<ContainerBookingReferenceDto> createBooking(ContainerBookingDto dto) {
		try {
			ContainerBooking entity = mapper.map(validate(dto), ContainerBooking.class);
			repository.save(entity);
			Mono<ContainerBookingReference> newReferenceBooking = getNewReferenceBooking(entity.getId());
			return Mono.just(mapper.map(newReferenceBooking, ContainerBookingReferenceDto.class));

		} catch (Exception e) {
			return Mono.error(() -> e);
		}
	}

	private Mono<ContainerBookingReference> getNewReferenceBooking(UUID entityId) {
		Long nextReferenceId = getNextReferenceNumber();
		String nextReference = String.format(BOOKING_REFERENCE_FORMAT, nextReferenceId);
		return bookingReferenceRepository.save(new ContainerBookingReference(nextReference, entityId));
	}

	private synchronized Long getNextReferenceNumber() {
		Long latest = entityReferenceRepository.getLatestReference(BOOKING_REFERENCE_NAME);
		long nextReference = latest + 1;
		// ensure that the key we got is valid as latest still
		Object updateReference = entityReferenceRepository.updateReference(BOOKING_REFERENCE_FORMAT, nextReference);
		if (updateReference != null) {
			return nextReference;
		} else {
			// Recursive, try again until we get a valid, unused and unique key.
			// We could have some retry logic to set a max attempts, I it will not be
			// implemented in this demo.
			return getNextReferenceNumber();
		}
	}

	public Mono<Boolean> isAvailable(ContainerBookingDto booking) {
		try {
			int containersAvailableInYard = getContainersAvailableInYard(booking); // unboxing
			return Mono.just(containersAvailableInYard > 0);
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

	private ContainerBookingDto validate(ContainerBookingDto dto) {
		return dto;
	}

}