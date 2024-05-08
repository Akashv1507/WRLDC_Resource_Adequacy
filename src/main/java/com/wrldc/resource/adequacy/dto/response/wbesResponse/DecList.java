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
public class DecList {
	@JsonProperty("SellerAcr")
	private String SellerAcr;
	@JsonProperty("SellerOnbarInput")
	private String SellerOnbarInput;
	@JsonProperty("NormativeDCOnbar")
	private String NormativeDCOnbar;
	@JsonProperty("DCOnBarForSchd")
	private String DCOnBarForSchd;
	@JsonProperty("DCOffBar")
	private String DCOffBar;
	@JsonProperty("SellerDCTotal")
	private String SellerDCTotal;
	@JsonProperty("RampUp")
	private String RampUp;
	@JsonProperty("RampDown")
	private String RampDown;
	@JsonProperty("Techmin")
	private String Techmin;
	@JsonProperty("Comments")
	private String Comments;
	@JsonProperty("RevisionNo")
	private Integer RevisionNo;
}
