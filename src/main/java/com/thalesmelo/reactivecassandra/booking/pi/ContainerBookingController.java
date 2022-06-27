package com.thalesmelo.reactivecassandra.booking.pi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thalesmelo.reactivecassandra.booking.ContainerBookingService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/bookings")
public class ContainerBookingController {

	@Autowired
	ContainerBookingService containerBookingService;

	@PostMapping("/available")
	public Mono<Boolean> isAvailable(@RequestBody ContainerBookingDto booking) {
		return containerBookingService.isAvailable(booking);
	}

	@PostMapping("/")
	public Mono<ContainerBookingReferenceDto> createBooking(@RequestBody ContainerBookingDto booking) {
		return containerBookingService.createBooking(booking);
	}
}