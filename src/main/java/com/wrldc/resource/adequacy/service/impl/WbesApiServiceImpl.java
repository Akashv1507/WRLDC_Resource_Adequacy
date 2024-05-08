package com.wrldc.resource.adequacy.service.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wrldc.resource.adequacy.dto.response.TimestampData;
import com.wrldc.resource.adequacy.dto.response.wbesResponse.WbesApiResponseDto;
import com.wrldc.resource.adequacy.dto.response.wbesResponse.WbesDataResp;
import com.wrldc.resource.adequacy.service.WbesApiService;

@Service
public class WbesApiServiceImpl implements WbesApiService {

	@Value("${wbes.api.username}")
	private String wbesUserName;
	@Value("${wbes.api.pwd}")
	private String wbesApiPwd;
	@Autowired
	private RestTemplate restTemplate;

	@Override
	public WbesDataResp fetchData(String wbesStateAcr, LocalDateTime startTimestamp,
			LocalDateTime endTimestamp) {

		// getting nearest timestamp in multiple of 15
		LocalDateTime loopIterTimestamp = startTimestamp;
		while (loopIterTimestamp.getMinute() % 15 != 0) {
			loopIterTimestamp = loopIterTimestamp.plusMinutes(1);
		}
		startTimestamp = loopIterTimestamp;
		loopIterTimestamp = endTimestamp;
		while (loopIterTimestamp.getMinute() % 15 != 0) {
			loopIterTimestamp = loopIterTimestamp.plusMinutes(1);
		}
		endTimestamp = loopIterTimestamp;
		List<Float> wbesDataList = new ArrayList<Float>();
		List<LocalDateTime> datetimeList = new ArrayList<LocalDateTime>();
		WbesDataResp wbesDataResp= new WbesDataResp();
		List<TimestampData> wbesSdlDataList = new ArrayList<TimestampData>();
		// pushing all 15 min timestamp to list and calulating length of datetimelist
		// array
		loopIterTimestamp = startTimestamp;
		Integer startTimeBlk = startTimestamp.getHour() * 4 + (startTimestamp.getMinute() / 15)+1;
		while (loopIterTimestamp.isBefore(endTimestamp)) {
			datetimeList.add(loopIterTimestamp);
			loopIterTimestamp = loopIterTimestamp.plusMinutes(15);
		}
		Integer datetimeListLength = datetimeList.size();
		// now creating startIndex and endIndex for wbesdatalist since it contain all
		// 96*NoOfDays
		Integer startIndexWbesDataList = startTimeBlk - 1;
		Integer endIndexWbesDataList = startIndexWbesDataList + datetimeListLength;
		// iterating through each day between startTimestamp and endtimestamp, fetching
		// schedule value, and appending to wbesDataList
		loopIterTimestamp = startTimestamp;
		while (loopIterTimestamp.isBefore(endTimestamp)) {
			DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			String currDateStr = loopIterTimestamp.format(dateFormatter);
			String apiUrl = String.format(
					"https://wbes.wrldc.in/WebAccess/GetFilteredSchdData?USER=%s&PASS=%s&DATE=%s&ACR=%s", wbesUserName,
					wbesApiPwd, currDateStr, wbesStateAcr);
			HttpHeaders apiHeaders = new HttpHeaders();
			apiHeaders.add("User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36 Edg/123.0.0.0");
			HttpEntity<MultiValueMap<String, String>> apiRequest = new HttpEntity<>(null, apiHeaders);
			try {
				ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.GET, apiRequest,
						String.class);

				if (responseEntity.getStatusCode() != HttpStatus.OK) {
					throw new RuntimeException(
							"Failed to obtain WBES API Data. HTTP Status: " + responseEntity.getStatusCode());
				}

				String responseBody = responseEntity.getBody();

				// Parse response body as JSON using JSONObject from org.json library
				//JSONObject jsonObject = new JSONObject(responseBody);

				// Convert JSON object to WbesApiResponseDto using ObjectMapper from Jackson
				// library
				ObjectMapper objectMapper = new ObjectMapper();
				WbesApiResponseDto wbesApiResponseDto = objectMapper.readValue(responseBody,
						WbesApiResponseDto.class);

				// Now you can use the wbesApiResponseDto object for further processing
				String sdlString = wbesApiResponseDto.getGroupWiseDataList().get(0).getNetScheduleSummary()
						.getNET_Total();
				List<Float> sdlList = Stream.of(sdlString.split(",")).map(String::trim).map(Float::parseFloat)
						.collect(Collectors.toList());
				wbesDataList.addAll(sdlList);
				wbesDataResp.setFullschdRevisionNo(wbesApiResponseDto.getFullschdRevisionNo());
			} catch (HttpClientErrorException | HttpServerErrorException ex) {
				HttpStatus statusCode = (HttpStatus) ex.getStatusCode();
				String responseBody = ex.getResponseBodyAsString();
				System.err.println("HTTP Error: " + statusCode + ", Response Body: " + responseBody);
				// Handle specific HTTP error statuses (4xx, 5xx) if needed

			} catch (IOException e) {
				System.err.println("Error parsing JSON response: " + e.getMessage());
				e.printStackTrace();
			}
			loopIterTimestamp = loopIterTimestamp.plusDays(1);
		}

		try {
			//filtering wbes float values list between startTimestamp and endTimestamp
			wbesDataList = wbesDataList.subList(startIndexWbesDataList, endIndexWbesDataList);
			for (int i = 0; i < datetimeList.size(); i++) {
				wbesSdlDataList.add(new TimestampData(datetimeList.get(i), wbesDataList.get(i)));
			}
			wbesDataResp.setWbesSdlDataList(wbesSdlDataList);
		} catch (IndexOutOfBoundsException e) {
			System.out.println("Exception thrown : " + e);
		}

		catch (IllegalArgumentException e) {
			System.out.println("Exception thrown : " + e);
		}
		
		return wbesDataResp;

	}

}
