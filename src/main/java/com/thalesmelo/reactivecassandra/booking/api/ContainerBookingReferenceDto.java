package com.thalesmelo.reactivecassandra.booking.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;


@Data
@ToString
@AllArgsConstructor
@Getter
public class ContainerBookingReferenceDto {

	@JsonProperty("“bookingRef”")
	private String bookingReference;

}
