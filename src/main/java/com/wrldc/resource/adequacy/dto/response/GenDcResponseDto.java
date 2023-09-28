package com.wrldc.resource.adequacy.dto.response;

//if returning few columns from repository entity use interface DTOs not class DTOs
public interface GenDcResponseDto {
	String getPlantName();
	 String getDateTime();
	 Integer getDcValue();
}
