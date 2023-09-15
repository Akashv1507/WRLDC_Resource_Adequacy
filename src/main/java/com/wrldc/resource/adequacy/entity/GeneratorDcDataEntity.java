package com.wrldc.resource.adequacy.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="intraday_dc_data" ,uniqueConstraints = { @UniqueConstraint(name = "UniquePlantNameAndUnit", columnNames = { "plant_id", "date_time" }) })
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class GeneratorDcDataEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	Integer id;
	@Column(name="date_time", nullable=false)
	private LocalDateTime dateTime;
	@Column(name="plant_name", nullable=false)
	private String plantName;
	@Column(name="dc_data", nullable=false)
	private Integer dcData;
	@JsonIgnore
	@ManyToOne()
	@JoinColumn(name="plant_id", nullable=false)
	private GeneratorMappingEntity plant;
}
