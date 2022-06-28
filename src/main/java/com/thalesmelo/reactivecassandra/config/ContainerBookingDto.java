package com.thalesmelo.reactivecassandra.config;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Builder
@AllArgsConstructor
public class ContainerBookingDto {
	private UUID id;

	private String containerType;

	private String origin; // Eg.: Southamptonmin 5, max 20

	private String destination; // Eg.: Singapore - min 5, max 20

	private int containerSize;// either 20 or 40

	private int quantity;// min 1, max 100

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "UTC")
	private Instant timestamp;

}
