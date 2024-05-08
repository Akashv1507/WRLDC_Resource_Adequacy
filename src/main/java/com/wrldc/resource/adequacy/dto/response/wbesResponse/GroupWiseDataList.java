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
public class GroupWiseDataList {
	@JsonProperty("Acronym")
	private String Acronym;
	@JsonProperty("fullschdList")
	private List<FullScheduleList> fullschdList;
	@JsonProperty("netScheduleSummary")
	private NetScheduleSummary netScheduleSummary;
	@JsonProperty("entReqList")
	private List<EntReqList> entReqList;
}
