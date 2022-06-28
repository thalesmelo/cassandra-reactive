package com.thalesmelo.reactivecassandra.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.thalesmelo.reactivecassandra.booking.ContainerBookingReference;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
class DataInitializer {

	@Autowired
	private final EntityReferenceRepository referenceRepository;

	public DataInitializer(EntityReferenceRepository referenceRepository) {
		this.referenceRepository = referenceRepository;
	}

	@EventListener(value = ContextRefreshedEvent.class)
	public void init() {
		log.info("Start data initialization...");
		this.referenceRepository.init(ContainerBookingReference.class.getSimpleName())//
				.subscribe(null, null, () -> log.info("Done initialization..."));
	}

}