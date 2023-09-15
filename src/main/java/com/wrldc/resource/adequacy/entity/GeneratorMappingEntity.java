package com.wrldc.resource.adequacy.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "mapping_table", uniqueConstraints = {
		@UniqueConstraint(name = "UniquePlantNameAndUnit", columnNames = { "plant_name", "unit_name" }) })
@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class GeneratorMappingEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;
	@Column(name = "plant_name", nullable = false)
	private String plantName;
	@Column(name = "unit_name", nullable = false)
	private String unitName;
	@Column(name = "state", nullable = false)
	private String state;
	@Column(name = "fuel_type", nullable = false)
	private String fuelType;
	@Column(name = "scada_id", nullable = false)
	private String scadaId;
	@Column(name = "intraday_dc_file_tag", nullable = false, unique = true)
	private String intradayDcFileTag;

	@OneToMany(mappedBy = "plant")
	private List<GeneratorDcDataEntity> dataEntities;

}
