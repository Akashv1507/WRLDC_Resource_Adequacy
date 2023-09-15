package com.wrldc.resource.adequacy.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;


import com.wrldc.resource.adequacy.dto.response.DcAndActualSingleGenResponseDto;
import com.wrldc.resource.adequacy.entity.GeneratorDcDataEntity;
import com.wrldc.resource.adequacy.entity.GeneratorMappingEntity;
import com.wrldc.resource.adequacy.repository.GeneratorDcDataRepositoty;
import com.wrldc.resource.adequacy.repository.GeneratorMappingRepository;
import com.wrldc.resource.adequacy.service.GeneratorService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class GeneratorServiceImpl implements GeneratorService {

	private GeneratorMappingRepository generatorMappingRepository;
	private GeneratorDcDataRepositoty generatorDcDataRepositoty;
	
	
	@Override
	public List<GeneratorMappingEntity> getAllGenMapping() {
		return generatorMappingRepository.findAll();
	}

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
		//removing seconds and minutes from current time and rounding to previous 15 min block, all operation will return copy
		LocalDateTime currentTime = LocalDateTime.of(2023, 9, 8, 8, 16,0 ).truncatedTo(ChronoUnit.MINUTES);
		while (currentTime.getMinute()%15 !=0) {
			currentTime=currentTime.minusMinutes(1);
		}
		String currentTimeString= currentTime.format(dateTimeFormatter);
		
		List<DcAndActualSingleGenResponseDto> intermediateResult=new ArrayList<DcAndActualSingleGenResponseDto>();
		List<DcAndActualSingleGenResponseDto> finalResult=new ArrayList<DcAndActualSingleGenResponseDto>();
		

		for(GeneratorMappingEntity obj:responsEntities)
		{
			DcAndActualSingleGenResponseDto singleObj= new DcAndActualSingleGenResponseDto();
			GeneratorDcDataEntity dcObj=generatorDcDataRepositoty.findByDateTimeAndPlant(currentTime, obj);
			if(dcObj==null) {
				continue;
			}
			
			singleObj.setPlantName(obj.getPlantName());
			singleObj.setDateTime(currentTimeString);
			singleObj.setDcValue(dcObj.getDcData());
			singleObj.setActualValue(0);
			
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
			finalSingleObj.setActualValue(0);
			int sumDc=0;
			//List<DcAndActualSingleGenResponseDto> dcAndActualSingleGenResponseDtoList= groupedMap.get(key);
			for(DcAndActualSingleGenResponseDto dto: entry.getValue()) {
				sumDc= sumDc+dto.getDcValue();
			} 
			finalSingleObj.setDcValue(sumDc);
			finalResult.add(finalSingleObj);
	    }

		return finalResult;
	}

	@Override
	public List<GeneratorDcDataEntity> getDataFromTime() {
		LocalDateTime currentTime = LocalDateTime.of(2023, 9, 6, 0, 0, 0 ).truncatedTo(ChronoUnit.MINUTES);
		while (currentTime.getMinute()%15 !=0) {
			currentTime=currentTime.minusMinutes(1);
		}

		return generatorDcDataRepositoty.findByDateTime(currentTime);
	}	

}
