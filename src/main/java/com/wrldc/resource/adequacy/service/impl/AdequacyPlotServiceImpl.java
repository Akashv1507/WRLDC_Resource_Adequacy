package com.wrldc.resource.adequacy.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;

import com.wrldc.resource.adequacy.dto.response.AdequacyPlotResponseDto;
import com.wrldc.resource.adequacy.dto.response.TimestampData;
import com.wrldc.resource.adequacy.dto.response.wbesResponse.WbesDataResp;
import com.wrldc.resource.adequacy.entity.TimestampDataInterface;
import com.wrldc.resource.adequacy.repository.misOracleRepo.DemandForecastRepository;
import com.wrldc.resource.adequacy.repository.misOracleRepo.ReForecastRepository;
import com.wrldc.resource.adequacy.repository.postgresRepo.GeneratorDcDataRepository;
import com.wrldc.resource.adequacy.service.AdequacyPlotsService;
import com.wrldc.resource.adequacy.service.WbesApiService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AdequacyPlotServiceImpl implements AdequacyPlotsService {
	
	private DemandForecastRepository demandForecastRepository;
	private ReForecastRepository reForecastRepository;
	private GeneratorDcDataRepository generatorDcDataRepository;
	private WbesApiService wbesApiService;
	
	@Override
	public AdequacyPlotResponseDto getAdequacyPlotData(String stateScadaId, String startTime, String endTime) {
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime startTimestamp= LocalDateTime.parse(startTime, dateTimeFormatter);
		LocalDateTime endTimestamp= LocalDateTime.parse(endTime, dateTimeFormatter);
		String stateName= "";
		String wbesStateAcrString="";
		if(stateScadaId.equals("WRLDCMP.SCADA1.A0046980")) {stateName= "MH"; wbesStateAcrString="MSEB_State";}
		else if(stateScadaId.equals("WRLDCMP.SCADA1.A0046957")) {stateName= "GJ"; wbesStateAcrString="GEB_State";}
		else if(stateScadaId.equals("WRLDCMP.SCADA1.A0046978")) {stateName= "MP"; wbesStateAcrString="MP_State";}
		else if (stateScadaId.equals("WRLDCMP.SCADA1.A0046945")) {stateName="CH"; wbesStateAcrString="CSEB_State";}
		
		AdequacyPlotResponseDto adequacyPlotResponseDto = new AdequacyPlotResponseDto();
		WbesDataResp wbesDataResp=wbesApiService.fetchData(wbesStateAcrString,startTimestamp, endTimestamp);
		List<TimestampDataInterface> demandForecastList = demandForecastRepository.getDemandForecast(stateScadaId, startTimestamp, endTimestamp);
		List<TimestampDataInterface> windForecastList=reForecastRepository.getReForecast(stateScadaId, "WIND", startTimestamp, endTimestamp);
		List<TimestampDataInterface> solarForecastList=reForecastRepository.getReForecast(stateScadaId, "SOLAR", startTimestamp, endTimestamp);
		List<TimestampDataInterface> thermalGenValueList=generatorDcDataRepository.geSumDcDataState(stateName, "Thermal", startTimestamp, endTimestamp);
		List<TimestampDataInterface> gasGenValueList=generatorDcDataRepository.geSumDcDataState(stateName, "Gas", startTimestamp, endTimestamp);
		List<TimestampDataInterface> hydroGenValueList=generatorDcDataRepository.geSumDcDataState(stateName, "Hydro", startTimestamp, endTimestamp);
		
		adequacyPlotResponseDto.setScheduleDataList(wbesDataResp.getWbesSdlDataList());
		adequacyPlotResponseDto.setDemForecastDataList(demandForecastList);
		adequacyPlotResponseDto.setSolarForecastDataList(solarForecastList);
		adequacyPlotResponseDto.setWindForecastDataList(windForecastList);
		adequacyPlotResponseDto.setThermalGenDataList(thermalGenValueList);
		adequacyPlotResponseDto.setHydroGenDataList(hydroGenValueList);
		adequacyPlotResponseDto.setGasGenDataList(gasGenValueList);
		adequacyPlotResponseDto.setFullschdRevisionNo(wbesDataResp.getFullschdRevisionNo());
		return adequacyPlotResponseDto;
	}

}
