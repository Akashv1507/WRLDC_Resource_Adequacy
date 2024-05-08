package com.wrldc.resource.adequacy.entity;

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
public class RealTimeOutageEntity {
	@Id
	Integer id;
	@Column(name="ELEMENTNAME", nullable=false)
	private String elementName;
	
}
