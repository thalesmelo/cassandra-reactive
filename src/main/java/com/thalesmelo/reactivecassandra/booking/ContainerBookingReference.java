package com.thalesmelo.reactivecassandra.booking;

import java.util.UUID;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@Table("bookings_reference")
public class ContainerBookingReference {

	@PrimaryKey()
	private String reference;

	private UUID id;

}
