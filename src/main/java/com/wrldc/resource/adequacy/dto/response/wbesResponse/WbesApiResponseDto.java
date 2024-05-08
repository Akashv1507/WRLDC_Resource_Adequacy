package com.wrldc.resource.adequacy.dto.response.wbesResponse;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WbesApiResponseDto {
	@JsonProperty("Date")
	private String Date;
	@JsonProperty("fullschdRevisionNo")
	private Integer fullschdRevisionNo;
	@JsonProperty("scheduleRemarks")
	private String scheduleRemarks;
	@JsonProperty("apiRemarks")
	private String apiRemarks;
	@JsonProperty("schedulePublishedTime")
	private String schedulePublishedTime;
	@JsonProperty("groupWiseDataList")
	private List<GroupWiseDataList> groupWiseDataList;
	@JsonProperty("decList")
	private List<DecList> decList;
}
