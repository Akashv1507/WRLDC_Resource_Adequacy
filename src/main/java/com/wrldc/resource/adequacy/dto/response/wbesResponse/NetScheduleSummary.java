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
public class NetScheduleSummary {
	@JsonProperty("ISGS")
	private String ISGS;
	@JsonProperty("MTOA")
	private String MTOA;
	@JsonProperty("STOA")
	private String STOA;
	@JsonProperty("LTA")
	private String LTA;
	@JsonProperty("IEX")
	private String IEX;
	@JsonProperty("PXI")
	private String PXI;
	@JsonProperty("HPX")
	private String HPX;
	@JsonProperty("URS")
	private String URS;
	@JsonProperty("AS_SHORTFALL")
	private String AS_SHORTFALL;
	@JsonProperty("AS_EMERGENCY")
	private String AS_EMERGENCY;
	@JsonProperty("SCED")
	private String SCED;
	@JsonProperty("REMC")
	private String REMC;
	@JsonProperty("BUNDLE_RE")
	private String BUNDLE_RE;
	@JsonProperty("GNA_CONTRACT")
	private String GNA_CONTRACT;
	@JsonProperty("TGNA_CONTRACT")
	private String TGNA_CONTRACT;
	@JsonProperty("RTM_PXI")
	private String RTM_PXI;
	@JsonProperty("RTM_IEX")
	private String RTM_IEX;
	@JsonProperty("RTM_HPX")
	private String RTM_HPX;
	@JsonProperty("GDAM_IEX")
	private String GDAM_IEX;
	@JsonProperty("GDAM_PXI")
	private String GDAM_PXI;
	@JsonProperty("GDAM_HPX")
	private String GDAM_HPX;
	@JsonProperty("HPDAM_IEX")
	private String HPDAM_IEX;
	@JsonProperty("HPDAM_PXI")
	private String HPDAM_PXI;
	@JsonProperty("HPDAM_HPX")
	private String HPDAM_HPX;
	@JsonProperty("MBAS_DAM_IEX")
	private String MBAS_DAM_IEX;
	@JsonProperty("MBAS_DAM_PXI")
	private String MBAS_DAM_PXI;
	@JsonProperty("MBAS_DAM_HPX")
	private String MBAS_DAM_HPX;
	@JsonProperty("MBAS_RTM_IEX")
	private String MBAS_RTM_IEX;
	@JsonProperty("MBAS_RTM_PXI")
	private String MBAS_RTM_PXI;
	@JsonProperty("MBAS_RTM_HPX")
	private String MBAS_RTM_HPX;
	@JsonProperty("NET_Total")
	private String NET_Total;
	
}
