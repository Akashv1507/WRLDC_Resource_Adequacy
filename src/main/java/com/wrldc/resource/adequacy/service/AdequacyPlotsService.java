package com.wrldc.resource.adequacy.service;


import com.wrldc.resource.adequacy.dto.response.AdequacyPlotResponseDto;

public interface AdequacyPlotsService {

	
	public AdequacyPlotResponseDto getAdequacyPlotData(String stateScadaId, String startTime, String endTime);
	
	

}
