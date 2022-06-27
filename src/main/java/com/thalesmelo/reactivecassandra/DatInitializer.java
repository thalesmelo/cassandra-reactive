package com.thalesmelo.reactivecassandra;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.thalesmelo.reactivecassandra.booking.ContainerBookingReference;
import com.thalesmelo.reactivecassandra.db.EntityReferenceRepository;
import com.thalesmelo.reactivecassandra.db.EntityReference;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
class DataInitializer {

	private final EntityReferenceRepository referenceRepository;

	public DataInitializer(EntityReferenceRepository referenceRepository) {
		this.referenceRepository = referenceRepository;
	}

	@EventListener(value = ContextRefreshedEvent.class)
	public void init() {
		log.info("Start data initialization...");
		this.referenceRepository //
				.deleteAll() //
				.then(Mono.just(createNewEntityReferenceEntry()))//
				.log() //
				.subscribe(null, null, () -> log.info("Done initialization..."));
	}

	private EntityReference createNewEntityReferenceEntry() {
		return new EntityReference(ContainerBookingReference.class.getSimpleName(), 0, 1);
	}

}