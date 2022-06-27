package com.thalesmelo.reactivecassandra.booking;

import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;

public interface ContainerBookingReferenceRepository
		extends ReactiveCassandraRepository<ContainerBookingReference, Long> {

}
