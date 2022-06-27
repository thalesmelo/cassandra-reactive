package com.thalesmelo.reactivecassandra.booking.pi;

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
@AllArgsConstructor
public class ContainerBookingDto {

	public enum ContainerType {
		DRY, REEFER
	}

	@Builder.Default
	private UUID id;

	@Column("container_type")
	private ContainerType containerType;

	@Column("container_size")
	private String containerSize;// either 20 or 40

	private String origin; // Eg.: Southamptonmin 5, max 20

	private String destination; // Eg.: Singapore - min 5, max 20

	private int quantity;// min 1, max 100

}
