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
public interface EntityReferenceRepository extends ReactiveCassandraRepository<EntityReference, Long> {

	@Query("UPDATE entity_references SET next=next+1 WHERE entity_name = ?0;")
	Mono<EntityReference> init(String entityClassSimpleName);
	
	@Query("UPDATE entity_references SET current=current+1 AND next=next+1 WHERE entity_name = ?0 and next = ?1 ;")
	Mono<EntityReference> updateReference(String entityClassSimpleName, long current);

	Mono<EntityReference> findByEntityName(String entityClassSimpleName);

}
