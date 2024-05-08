package com.wrldc.resource.adequacy.dto.response.wbesResponse;

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
public class FullScheduleList {
	@JsonProperty("BuyerAcr")
	private String BuyerAcr;
	@JsonProperty("BuyerStateName")
	private String BuyerStateName;
	@JsonProperty("BuyerWBESParentStateAcronym")
	private String BuyerWBESParentStateAcronym;
	@JsonProperty("SellerAcr")
	private String SellerAcr;
	@JsonProperty("SellerStateName")
	private String SellerStateName;
	@JsonProperty("SellerWBESParentStateAcronym")
	private String SellerWBESParentStateAcronym;
	@JsonProperty("TraderAcr")
	private String TraderAcr;
	@JsonProperty("ApprovalNo")
	private String ApprovalNo;	
	@JsonProperty("ScheduleAmount")
	private String ScheduleAmount;
	@JsonProperty("BuyerAmount")
	private String BuyerAmount;
	@JsonProperty("SellerAmount")
	private String SellerAmount;
	@JsonProperty("ScheduleTypeName")
	private String ScheduleTypeName;
	@JsonProperty("SubScheduleTypeName")
	private String SubScheduleTypeName;
	@JsonProperty("LinkName")
	private String LinkName;																																					
}
