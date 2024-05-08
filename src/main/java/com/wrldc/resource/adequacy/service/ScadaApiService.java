package com.wrldc.resource.adequacy.service;

import java.util.List;

import com.wrldc.resource.adequacy.dto.response.TimestampData;

public interface ScadaApiService {
	public List<TimestampData> fetchData(String measId, String startDt, String endDt);
	
	public List<TimestampData> parseApiResponse(List<Number> scadaDataList);
}

