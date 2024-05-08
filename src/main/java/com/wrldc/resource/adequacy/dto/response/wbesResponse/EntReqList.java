package com.wrldc.resource.adequacy.dto.response.wbesResponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EntReqList {
	@JsonProperty("SellerAcr")
	private String SellerAcr;
	@JsonProperty("BuyerAcr")
	private String BuyerAcr;
	@JsonProperty("EntOnBar")
	private String EntOnBar;
	@JsonProperty("EntOffBar")
	private String EntOffBar;
	@JsonProperty("ReqOnBar")
	private String ReqOnBar;
	@JsonProperty("ReqOffBar")
	private String ReqOffBar;
}
