package com.wrldc.resource.adequacy.service;

import java.time.LocalDateTime;
import java.util.List;

import com.wrldc.resource.adequacy.dto.response.TimestampData;
import com.wrldc.resource.adequacy.dto.response.wbesResponse.WbesDataResp;

public interface WbesApiService {
	public WbesDataResp fetchData(String wbesStateAcr, LocalDateTime startTimestamp, LocalDateTime endTimestamp);
}
