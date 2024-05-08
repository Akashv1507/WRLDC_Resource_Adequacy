package com.wrldc.resource.adequacy.dto.response;

//if returning few columns from repository entity use interface DTOs not class DTOs
public interface GenSchResponseDto {
	String getPlantName();
	 String getDateTime();
	 Integer getSchValue();
}
