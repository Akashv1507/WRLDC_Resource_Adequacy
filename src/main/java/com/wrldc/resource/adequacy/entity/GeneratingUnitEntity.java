package com.wrldc.resource.adequacy.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class GeneratingUnitEntity {
	@Id
	Integer id;
	@Column(name="unit_name", nullable=false)
	private String unitName;
	@Column(name="installed_capacity", nullable=false)
	private Integer installedCapacity ;
}
