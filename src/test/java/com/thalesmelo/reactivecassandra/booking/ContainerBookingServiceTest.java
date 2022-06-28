package com.thalesmelo.reactivecassandra.booking;

import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.thalesmelo.reactivecassandra.booking.ContainerBooking.ContainerType;
import com.thalesmelo.reactivecassandra.booking.api.ContainerBookingDto;
import com.thalesmelo.reactivecassandra.db.EntityReferenceRepository;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
		"external.service.check.container.availabilty.url=https://maersk.com/api/bookings/checkAvailable" })
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
	public void givenMockingIsDoneByMockito_whenGetIsCalled_shouldReturnMockedObject() {

		Mockito.when(externalService.getForEntity("https://maersk.com/api/bookings/checkAvailable", Integer.class))
				.thenReturn(new ResponseEntity(1, HttpStatus.OK));

		ContainerBookingDto booking = new ContainerBookingDto(UUID.randomUUID(), ContainerType.DRY.name(), "origin",
				"destination", 20, 10, Instant.now());
		Mono<AvailableBookingDto> mono = service.isAvailable(booking);

		StepVerifier.create(mono.log()).expectNext(new AvailableBookingDto(true)).verifyComplete();
	}
}