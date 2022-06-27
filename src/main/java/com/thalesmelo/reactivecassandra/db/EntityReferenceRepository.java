package com.thalesmelo.reactivecassandra.db;

import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;

public interface EntityReferenceRepository extends ReactiveCassandraRepository<EntityReference, Long> {

	@Query("UPDATE entity_references SET current=current+1 AND next=next+1 WHERE entity_name = ?0 and current = ?1 ;")
	Object updateReference(String entityClassSimpleName, long current);

	@Query("select current from entity_references WHERE entity_name = ?0 ;")
	Long getLatestReference(String entityClassSimpleName);

}
