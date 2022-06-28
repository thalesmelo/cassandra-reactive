package com.thalesmelo.reactivecassandra.booking;

import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;

/**
 * This repository exists because in cassandra you have to use non recommended
 * strategies search by other fields that are not primary key.
 * 
 * This happens because it could be super slow, to do so using the allow
 * filtering annotation we create a table where the booking reference is a
 * primary key and the uuid of the booking is a secondary key.
 * 
 * @author tbm
 *
 */
public interface ContainerBookingReferenceRepository
		extends ReactiveCassandraRepository<ContainerBookingReference, Long> {

}
