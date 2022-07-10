package com.thalesmelo.reactivecassandra.booking;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.thalesmelo.reactivecassandra.booking.ContainerBooking.ContainerType;
import com.thalesmelo.reactivecassandra.booking.api.ContainerBookingReferenceDto;
import com.thalesmelo.reactivecassandra.config.ContainerBookingDto;
import com.thalesmelo.reactivecassandra.db.EntityReference;
import com.thalesmelo.reactivecassandra.db.EntityReferenceRepository;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
		"external.service.check.container.availabilty.url=https://company.com/api/bookings/checkAvailable" })
public class ContainerBookingServiceTest {

	@MockBean
	private ContainerBookingRepository repository;

	@MockBean
	private ContainerBookingReferenceRepository bookingReferenceRepository;

	@MockBean
	private EntityReferenceRepository entityReferenceRepository;

	@MockBean
	private RestTemplate externalService;

	@Autowired
	private ContainerBookingService service = new ContainerBookingService();

	@Test
	public void given_validBooking_when_isAvailableIsCalled_shouldReturn_TrueAvailableBooking() {

		Mockito.when(externalService.postForEntity(any(), any(), any())).thenAnswer(i -> ResponseEntity.ok(2));

		ContainerBookingDto booking = new ContainerBookingDto(UUID.randomUUID(), ContainerType.DRY.name(), "origin",
				"destination", 20, 10, Instant.now());
		Mono<AvailableBookingDto> mono = service.isAvailable(booking);

		StepVerifier.create(mono).expectNext(new AvailableBookingDto(true)).verifyComplete();
	}

	@Test
	public void given_InvalidTypeBooking_when_CreateBookingIsCalled_shouldThrowException() {
		ContainerBookingDto booking = new ContainerBookingDto(UUID.randomUUID(), "NONE", "origin",
				"destination", 20, 10, Instant.now());

		IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
				() -> service.createBooking(booking),
				"Expected service.createBooking(booking) to throw, but it didn't");
	}
	
	@Test
	public void given_InvalidContainerSizeBooking_when_CreateBookingIsCalled_shouldThrowException() {
		ContainerBookingDto booking = new ContainerBookingDto(UUID.randomUUID(), ContainerType.DRY.name(), "origin",
				"destination", 5, 10, Instant.now());
		
		IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
				() -> service.createBooking(booking),
				"Expected service.createBooking(booking) to throw, but it didn't");
	}

	@Test
	public void given_validBooking_when_CreateBookingIsCalled_shouldReturn_Booking() {

		Mockito.when(entityReferenceRepository.updateReference(anyString(), anyLong()))
				.thenAnswer(i -> Mono.just(getReference(2L)));
		Mockito.when(entityReferenceRepository.findByEntityName(anyString()))
				.thenAnswer(i -> Mono.just(getReference(1L)));

		Mockito.when(repository.save(any())).thenAnswer(i -> Mono.just(i.getArguments()[0]));
		Mockito.when(bookingReferenceRepository.save(any())).thenAnswer(i -> Mono.just(i.getArguments()[0]));

		ContainerBookingDto booking = new ContainerBookingDto(UUID.randomUUID(), ContainerType.DRY.name(), "origin",
				"destination", 20, 10, Instant.now());
		Mono<ContainerBookingReferenceDto> mono = service.createBooking(booking);

		StepVerifier.create(mono).expectNext(new ContainerBookingReferenceDto("0000000000000000002")).verifyComplete();
	}


	private EntityReference getReference(Long current) {
		return new EntityReference(ContainerBookingReference.class.getSimpleName(), current, current + 1);
	}
}