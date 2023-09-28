package com.wrldc.resource.adequacy.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import com.wrldc.resource.adequacy.entity.ScadaData;

public interface ScadaApiService {
	public List<ScadaData> fetchData(String measId, String startDt, String endDt);
	
	public List<ScadaData> parseApiResponse(List<Number> scadaDataList);
}

