package com.thalesmelo.reactivecassandra.booking;

import com.thalesmelo.reactivecassandra.booking.ContainerBooking.ContainerType;
import com.thalesmelo.reactivecassandra.booking.api.ContainerBookingReferenceDto;
import com.thalesmelo.reactivecassandra.config.ContainerBookingDto;

public class BookingModelMapper {

	public ContainerBooking map(ContainerBookingDto dto) {
		return new ContainerBooking(dto.getId(), ContainerType.valueOf(dto.getContainerType()), dto.getOrigin(),
				dto.getDestination(), dto.getContainerSize(), dto.getQuantity(), dto.getTimestamp());
	}

	public ContainerBookingReferenceDto map(ContainerBookingReference reference) {
		return new ContainerBookingReferenceDto(reference.getReference());
	}

}
