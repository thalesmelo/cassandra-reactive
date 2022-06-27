package com.thalesmelo.reactivecassandra.db;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.CassandraType.Name;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@AllArgsConstructor
@Getter
@Table("entity_references")
public class EntityReference {

	@PrimaryKeyColumn(name = "entity_name", type = PrimaryKeyType.PARTITIONED)
	private String entityName;

	@CassandraType(type = Name.COUNTER)
	private long current = 0;

	@CassandraType(type = Name.COUNTER)
	private long next = 1;

}
