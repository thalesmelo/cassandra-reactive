package com.thalesmelo.reactivecassandra.db;

import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;

/**
 * This only exists because we cannot get auto increment keys in a easy way in
 * cassandra.
 * 
 * Then we use this auxiliary table to control the auto increment key
 * generation and allocation
 * 
 * @author tbm
 *
 */
public interface EntityReferenceRepository extends ReactiveCassandraRepository<EntityReference, Long> {

	@Query("UPDATE EntityReference SET current=current+1 AND next=next+1 WHERE entityName = ?0 and next = ?1 ;")
	EntityReference updateReference(String entityClassSimpleName, long current);

	@Query("select current from entity_references WHERE entity_name = ?0 ;")
	Long getLatestReference(String entityClassSimpleName);

}
