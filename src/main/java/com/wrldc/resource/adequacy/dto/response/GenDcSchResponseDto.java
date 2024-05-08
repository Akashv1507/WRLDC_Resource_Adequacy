package com.wrldc.resource.adequacy.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GenDcSchResponseDto {
	private String plantName;
	private String dateTime;
	private Integer dcValue;
	private Integer schValue;
	
}
