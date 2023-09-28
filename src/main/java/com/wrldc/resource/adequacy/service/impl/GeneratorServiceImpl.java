package com.wrldc.resource.adequacy.service.impl;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.wrldc.resource.adequacy.dto.response.DcAndActualSingleGenResponseDto;
import com.wrldc.resource.adequacy.dto.response.GenDcResponseDto;
import com.wrldc.resource.adequacy.entity.GeneratorDcDataEntity;
import com.wrldc.resource.adequacy.entity.GeneratorMappingEntity;
import com.wrldc.resource.adequacy.entity.ScadaData;
import com.wrldc.resource.adequacy.repository.GeneratorDcDataRepositoty;
import com.wrldc.resource.adequacy.repository.GeneratorMappingRepository;
import com.wrldc.resource.adequacy.service.GeneratorService;
import com.wrldc.resource.adequacy.service.ScadaApiService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class GeneratorServiceImpl implements GeneratorService {

	private GeneratorMappingRepository generatorMappingRepository;
	private GeneratorDcDataRepositoty generatorDcDataRepositoty;
	private ScadaApiService scadaApiService;
	
	
	@Override
	public List<String> getStateList() {
		// TODO Auto-generated method stub
		return generatorMappingRepository.findDistinctState();
	}

	@Override
	public List<DcAndActualSingleGenResponseDto> getDcAndActualDataByState(String stateName) {
		
		List<GeneratorMappingEntity> responsEntities=  generatorMappingRepository.findByState(stateName);
		// Create DateTimeFormatter instance with specified format
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		//removing seconds from current time and rounding to previous 15 min block, all operation will return copy
		LocalDateTime currentTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
		while (currentTime.getMinute()%15 !=0) {
			currentTime=currentTime.minusMinutes(1);
		}
		String currentTimeString= currentTime.format(dateTimeFormatter);
		String currentDateString = currentTime.format(dateFormatter);
		
		List<DcAndActualSingleGenResponseDto> intermediateResult=new ArrayList<DcAndActualSingleGenResponseDto>();
		List<DcAndActualSingleGenResponseDto> finalResult=new ArrayList<DcAndActualSingleGenResponseDto>();
		

		for(GeneratorMappingEntity obj:responsEntities)
		{
			DcAndActualSingleGenResponseDto singleObj= new DcAndActualSingleGenResponseDto();
			GeneratorDcDataEntity dcObj=generatorDcDataRepositoty.findByDateTimeAndPlant(currentTime, obj);
			if(dcObj==null) {
				continue;
			}
			List<String> listOfScadaId= Stream.of(obj.getScadaId().split(",")).map(String::trim).collect(Collectors.toList());
			Double sumActGenDoubleVal= 0.0;
			for (String scadaIdString :listOfScadaId ) {
				List<ScadaData> scadaDataList = scadaApiService.fetchData(scadaIdString, currentDateString, currentDateString);
				if(scadaDataList.size()!=0) {
				sumActGenDoubleVal = sumActGenDoubleVal + Math.abs(scadaDataList.get(scadaDataList.size()-1).getValue());
				}
			}
			Long LongsumActGenLongVal=Double.valueOf(sumActGenDoubleVal).longValue();
			singleObj.setPlantName(obj.getPlantName());
			singleObj.setDateTime(currentTimeString);
			singleObj.setDcValue(dcObj.getDcData());
			singleObj.setActualValue(LongsumActGenLongVal);
			
			intermediateResult.add(singleObj);
		}
		
		Map<String, List<DcAndActualSingleGenResponseDto>> groupedMap = intermediateResult
			    .stream()
			    .collect(
			        Collectors.groupingBy(DcAndActualSingleGenResponseDto::getPlantName));
		
		for (Entry<String, List<DcAndActualSingleGenResponseDto>> entry : groupedMap.entrySet()) {
			DcAndActualSingleGenResponseDto finalSingleObj= new DcAndActualSingleGenResponseDto();
			finalSingleObj.setPlantName(entry.getKey());
			finalSingleObj.setDateTime(currentTimeString);
			int sumDc=0;
			long sumAct =0;
			
			for(DcAndActualSingleGenResponseDto dto: entry.getValue()) {
				sumDc= sumDc+dto.getDcValue();
				sumAct = sumAct + dto.getActualValue();
			} 
			finalSingleObj.setDcValue(sumDc);
			finalSingleObj.setActualValue(sumAct);
			finalResult.add(finalSingleObj);
	    }

		return finalResult;
	}

	@Override
	public List<GenDcResponseDto> getDCByState(String genName) {
		LocalDateTime currentTime = LocalDateTime.now();
		LocalDateTime startTime = currentTime.with(LocalTime.MIN);
		LocalDateTime endTime = currentTime.with(LocalTime.MAX);
		List<GenDcResponseDto> generatorDcDataEntityList =generatorDcDataRepositoty.getAllDayDataByGenName(genName, startTime, endTime);
		return generatorDcDataEntityList;
	}

	

}
