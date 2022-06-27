package com.thalesmelo.reactivecassandra.booking;

import java.util.UUID;

import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ContainerBookingRepository extends ReactiveCassandraRepository<ContainerBooking, UUID> {

}
