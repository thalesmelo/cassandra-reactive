package com.thalesmelo.reactivecassandra.db;

import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Mono;

/**
 * This only exists because we cannot get auto increment keys in a easy way in
 * cassandra.
 * 
 * Then we use this auxiliary table to control the auto increment key generation
 * and allocation
 * 
 * @author tbm
 *
 */
@Repository
public interface EntityReferenceRepository extends ReactiveCassandraRepository<EntityReference, Long> {

	@Query("UPDATE EntityReference SET current=current+1 AND next=next+1 WHERE entityName = ?0 and next = ?1 ;")
	Mono<EntityReference> updateReference(String entityClassSimpleName, long current);

	Mono<EntityReference> findByName(String entityClassSimpleName);

}
