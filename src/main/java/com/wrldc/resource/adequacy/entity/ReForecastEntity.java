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
public class ReForecastEntity {
	@Id
	Integer id;
	@Column(name="time_stamp", nullable=false)
	private  LocalDateTime timeStamp;
	@Column(name="entity_tag", nullable=false)
	private String entityTag;
	@Column(name="re_type", nullable=false)
	private String reType;
	@Column(name="re_forecasted_value", nullable=false)
	private double reForecastedValue ;
}
