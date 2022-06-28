package com.thalesmelo.reactivecassandra.booking;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("bookings")
public class ContainerBooking {

	public enum ContainerType {
		DRY, REEFER
	}

	@PrimaryKey()
	@Builder.Default
	private UUID id = UUID.randomUUID();

	@Column("container_type")
	private ContainerType containerType;

	private String origin; // Eg.: Southamptonmin 5, max 20

	private String destination; // Eg.: Singapore - min 5, max 20

	@Column("container_size")
	private int containerSize;// either 20 or 40

	private int quantity;// min 1, max 100

	@Builder.Default
	private Instant timestamp = Instant.now();

}
