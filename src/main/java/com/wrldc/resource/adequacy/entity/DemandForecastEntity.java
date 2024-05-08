package com.wrldc.resource.adequacy.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class DemandForecastEntity {
	@Id
	Integer id;
	@Column(name="time_stamp", nullable=false)
	private  LocalDateTime timeStamp;
	@Column(name="entity_tag", nullable=false)
	private String entityTag;
	@Column(name="forecasted_demand_value", nullable=false)
	private double forecastedDemandValue ;
}
