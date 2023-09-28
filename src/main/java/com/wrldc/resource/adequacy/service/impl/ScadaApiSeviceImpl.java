package com.wrldc.resource.adequacy.service.impl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.wrldc.resource.adequacy.entity.ScadaData;
import com.wrldc.resource.adequacy.service.ScadaApiService;

@Service
public class ScadaApiSeviceImpl implements ScadaApiService {
	
	@Value("${scada.api.tokenUrl}")
	private  String tokenUrl;
	@Value("${scada.api.apiBaseUrl}")
	private  String apiBaseUrl;
	@Value("${scada.api.clientId}")
	private  String clientId ;
	@Value("${scada.api.clientSecret}")
	private  String clientSecret;
	

	@Autowired
	private RestTemplate restTemplate;

	@Override
	public List<ScadaData> fetchData(String measId, String startDt, String endDt) {

		// Construct the API URL
		String apiUrl = String.format("%s/api/scadadata/%s/%s/%s", apiBaseUrl, measId, startDt, endDt);

		// Step A, B - Request for Access Token
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.setBasicAuth(clientId, clientSecret);
		MultiValueMap<String, String> requestBody = new LinkedMultiValueMap();
		requestBody.add("grant_type", "client_credentials");
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(requestBody, headers);
		ResponseEntity<Map> responseEntity = restTemplate.exchange(tokenUrl, HttpMethod.POST, request, Map.class);

		if (responseEntity.getStatusCode() != HttpStatus.OK) {
			throw new RuntimeException("Failed to obtain access token");
		}

		// Step B: Fetch data from the SCADA API using the access token
		Map accessTokenMap = responseEntity.getBody();
		String accessToken=(String) accessTokenMap.get("access_token");
		HttpHeaders apiHeaders = new HttpHeaders();
		apiHeaders.setBearerAuth(accessToken);
		HttpEntity<MultiValueMap<String, String>> apiRequest = new HttpEntity<>(null, apiHeaders);

		ResponseEntity<List> scadaApiResponseEntity = restTemplate.exchange(apiUrl, HttpMethod.GET, apiRequest, List.class);
		if (scadaApiResponseEntity.getStatusCode() != HttpStatus.OK) {
			throw new RuntimeException("Failed to obtain Scada Data");
		}
		List<Number> apiDataList=scadaApiResponseEntity.getBody();
		// Parse the response and convert it into a list of ScadaData objects
		List<ScadaData> scadaDataList = parseApiResponse(apiDataList);

		// System.out.print(apiResponse);

		return scadaDataList;
	}

	public List<ScadaData> parseApiResponse(List<Number> apiDataList) {
		// Implement logic to parse the API response and convert it into a
		// List<ScadaData>
		List<ScadaData> scadaDataList = new ArrayList<>();
		for (int i = 0; i < apiDataList.size(); i += 2) {
			LocalDateTime timestamp = LocalDateTime.ofInstant(Instant.ofEpochMilli((long) apiDataList.get(i)),TimeZone.getDefault().toZoneId());
			//to handle integer value, first converting to string and then to double
			double value = Double.valueOf(apiDataList.get(i+1).toString());
			scadaDataList.add(new ScadaData(timestamp, value));
		}
		return scadaDataList;
	}

}
