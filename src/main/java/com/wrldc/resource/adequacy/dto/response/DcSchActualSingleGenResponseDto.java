package com.wrldc.resource.adequacy.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DcSchActualSingleGenResponseDto {

	private String plantName;
	private Integer installedCapacity;
	private String dateTime;
	private Integer dcValue;
	private Integer schValue;
	private Integer actualValue;
	private Integer outageCapacity;
	private Integer diffVal;
	private Integer runningCapacity;
	private Float auxConsumption;
	private Integer partialOutage;
	private String fuelType;
}
