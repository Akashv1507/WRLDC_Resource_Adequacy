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

import com.wrldc.resource.adequacy.dto.response.DcSchActualSingleGenResponseDto;
import com.wrldc.resource.adequacy.dto.response.GenDcResponseDto;
import com.wrldc.resource.adequacy.dto.response.GenDcSchResponseDto;
import com.wrldc.resource.adequacy.dto.response.GenSchResponseDto;
import com.wrldc.resource.adequacy.dto.response.TimestampData;
import com.wrldc.resource.adequacy.entity.GeneratingUnitEntity;
import com.wrldc.resource.adequacy.entity.GeneratorDcDataEntity;
import com.wrldc.resource.adequacy.entity.GeneratorMappingEntity;
import com.wrldc.resource.adequacy.entity.GeneratorSchDataEntity;
import com.wrldc.resource.adequacy.repository.oracleRepo.GeneratingUnitRepository;
import com.wrldc.resource.adequacy.repository.oracleRepo.RealTimeOutageRepository;
import com.wrldc.resource.adequacy.repository.postgresRepo.GeneratorDcDataRepository;
import com.wrldc.resource.adequacy.repository.postgresRepo.GeneratorMappingRepository;
import com.wrldc.resource.adequacy.repository.postgresRepo.GeneratorSchDataRepository;
import com.wrldc.resource.adequacy.service.GeneratorService;
import com.wrldc.resource.adequacy.service.ScadaApiService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class GeneratorServiceImpl implements GeneratorService {

	private GeneratorMappingRepository generatorMappingRepository;
	private GeneratorDcDataRepository generatorDcDataRepository;
	private GeneratorSchDataRepository generatorSchDataRepository;
	private ScadaApiService scadaApiService;
	private GeneratingUnitRepository generatingUnitRepository;
	private RealTimeOutageRepository realTimeOutageRepository;
	
	
	@Override
	public List<String> getStateList() {
		// TODO Auto-generated method stub
		return generatorMappingRepository.findDistinctState();
	}

	@Override
	public List<DcSchActualSingleGenResponseDto> getDcSchActualDataByState(String stateName) {
		
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
		
		List<DcSchActualSingleGenResponseDto> intermediateResult=new ArrayList<DcSchActualSingleGenResponseDto>();
		List<DcSchActualSingleGenResponseDto> finalResult=new ArrayList<DcSchActualSingleGenResponseDto>();
		

		for(GeneratorMappingEntity obj:responsEntities)
		{
			DcSchActualSingleGenResponseDto singleObj= new DcSchActualSingleGenResponseDto();
			GeneratorDcDataEntity dcObj=generatorDcDataRepository.findByDateTimeAndPlant(currentTime, obj);
			GeneratorSchDataEntity schObj=generatorSchDataRepository.findByDateTimeAndPlant(currentTime, obj);
			
			// if fetched DC or Sch data for current blk is null then initializing its dc/sch val to 0
			if(dcObj==null) {
				dcObj= new GeneratorDcDataEntity(1, currentTime, obj.getPlantName() , 0, null);
			}
			if(schObj==null) {
				schObj= new GeneratorSchDataEntity(1, currentTime, obj.getPlantName() , 0, null);
			}
			Integer dcVal=dcObj.getDcData();
			Integer schVal= schObj.getSchData();
			String fuelType= obj.getFuelType(); 
			//fetching outage capcity, it may contain more than 1 unit_name
			Integer outageCapacity =0;
			List<String> listOfUnitName= Stream.of(obj.getOutageUnitName().split("#")).map(String::trim).collect(Collectors.toList());
			for (String unitName :listOfUnitName ) {
				Integer isUnitOut=realTimeOutageRepository.checkUnitIsOut(unitName, currentTimeString);
				if(isUnitOut.intValue()==1) {
					GeneratingUnitEntity unitInfo=generatingUnitRepository.getUnitInfo(unitName);
					outageCapacity= outageCapacity+unitInfo.getInstalledCapacity();
				}
			}
			//fetching scada data for particular current obj, it may contain more than 1 scada id
			List<String> listOfScadaId= Stream.of(obj.getScadaId().split(",")).map(String::trim).collect(Collectors.toList());
			Double sumActGenDoubleVal= 0.0;
			for (String scadaIdString :listOfScadaId ) {
				List<TimestampData> scadaDataList = scadaApiService.fetchData(scadaIdString, currentDateString, currentDateString);
				if(scadaDataList.size()!=0) {
				sumActGenDoubleVal = sumActGenDoubleVal + Math.abs(scadaDataList.get(scadaDataList.size()-1).getValue());
				}
			}
			int LongsumActGenLongVal=Double.valueOf(sumActGenDoubleVal).intValue();
			// running capacity and partial outage
			Integer runningCapacity = obj.getInstalledCapacity()-outageCapacity;
			float auxConsumption=obj.getAuxConsumption();
			float effectiveRunningCapacity=(1-(auxConsumption/100))*runningCapacity;
			//IF DC =0 THEN DC=RUNNING CAPACITY*AUX CONSUMPTION
			if(dcVal==0) {
				dcVal = Math.round(effectiveRunningCapacity);
			}
			Integer partialOutage = Math.round(effectiveRunningCapacity-dcVal);
			
			singleObj.setPlantName(obj.getPlantName());
			singleObj.setDateTime(currentTimeString);
			singleObj.setDcValue(dcVal);
			singleObj.setSchValue(schVal);
			singleObj.setActualValue(LongsumActGenLongVal);
			singleObj.setOutageCapacity(outageCapacity);
			singleObj.setInstalledCapacity(obj.getInstalledCapacity());
			singleObj.setRunningCapacity(runningCapacity);
			singleObj.setPartialOutage(partialOutage);
			singleObj.setAuxConsumption(auxConsumption);
			singleObj.setFuelType(fuelType);
			intermediateResult.add(singleObj);
		}
		
		Map<String, List<DcSchActualSingleGenResponseDto>> groupedMap = intermediateResult
			    .stream()
			    .collect(
			        Collectors.groupingBy(DcSchActualSingleGenResponseDto::getPlantName));
		
		for (Entry<String, List<DcSchActualSingleGenResponseDto>> entry : groupedMap.entrySet()) {
			DcSchActualSingleGenResponseDto finalSingleObj= new DcSchActualSingleGenResponseDto();
			finalSingleObj.setPlantName(entry.getKey());
			finalSingleObj.setDateTime(currentTimeString);
			Integer sumDc=0;
			Integer sumSch=0;
			Integer sumOutageCapacity=0;
			Integer sumAct =0;
			Integer sumInstalledCap=0;
			Integer runningCapacity=0;
			Integer partialOutage=0;
			Float auxConsumption=0f;
			Integer countNo=0;
			String fuelType="";
			for(DcSchActualSingleGenResponseDto dto: entry.getValue()) {
				sumDc= sumDc+dto.getDcValue();
				sumAct = sumAct + dto.getActualValue();
				sumSch = sumSch + dto.getSchValue();
				sumInstalledCap = sumInstalledCap +dto.getInstalledCapacity();
				sumOutageCapacity= sumOutageCapacity+dto.getOutageCapacity();
				runningCapacity= runningCapacity+ dto.getRunningCapacity();
				partialOutage= partialOutage+dto.getPartialOutage();
				auxConsumption= auxConsumption+ (float)dto.getAuxConsumption();	
				fuelType=dto.getFuelType();
				countNo = countNo +1;
				
			} 
			float avgAuxConsumption= auxConsumption/countNo;
//			auxConsumption =Math.round(auxConsumption*100.0L)/100.0;
			finalSingleObj.setDcValue(sumDc);
			finalSingleObj.setSchValue(sumSch);
			finalSingleObj.setActualValue(sumAct);
			finalSingleObj.setDiffVal(sumDc-sumSch);
			finalSingleObj.setOutageCapacity(sumOutageCapacity);
			finalSingleObj.setInstalledCapacity(sumInstalledCap);
			finalSingleObj.setRunningCapacity(runningCapacity);
			finalSingleObj.setPartialOutage(partialOutage);
			finalSingleObj.setAuxConsumption(avgAuxConsumption);
			finalSingleObj.setFuelType(fuelType);
			finalResult.add(finalSingleObj);
	    }

		return finalResult;
	}

	@Override
	public List<GenDcSchResponseDto> getDcSchByGen(String genName) {
		LocalDateTime currentTime = LocalDateTime.now();
		LocalDateTime startTime = currentTime.with(LocalTime.MIN);
		LocalDateTime endTime = currentTime.with(LocalTime.MAX);
		List<GenDcSchResponseDto> genDcSchResponseList=new ArrayList<GenDcSchResponseDto>();
		List<GenDcResponseDto> generatorDcDataEntityList =generatorDcDataRepository.getAllDayDataByGenName(genName, startTime, endTime);
		List<GenSchResponseDto> generatorSchDataEntityList =generatorSchDataRepository.getAllDayDataByGenName(genName, startTime, endTime);
		
		for (GenDcResponseDto genDcDataSingleBlk :generatorDcDataEntityList ) {
			GenDcSchResponseDto blkGenDcSchResponse=new GenDcSchResponseDto();
			String timestamp=genDcDataSingleBlk.getDateTime();
			Integer schVal= 0;
			for (GenSchResponseDto genSchDataSingleBlk :generatorSchDataEntityList ) {
				String schTimestamp= genSchDataSingleBlk.getDateTime();
				if (schTimestamp.equalsIgnoreCase(timestamp)) {
					schVal= genSchDataSingleBlk.getSchValue();
					break;
				}
			}
			blkGenDcSchResponse.setDateTime(genDcDataSingleBlk.getDateTime());
			blkGenDcSchResponse.setPlantName(genName);
			blkGenDcSchResponse.setDcValue(genDcDataSingleBlk.getDcValue());
			blkGenDcSchResponse.setSchValue(schVal);
			genDcSchResponseList.add(blkGenDcSchResponse);
					}
		return genDcSchResponseList;
	}

	

}
