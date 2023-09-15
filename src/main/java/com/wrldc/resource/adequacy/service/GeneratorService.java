package com.wrldc.resource.adequacy.service;


import java.util.List;
import com.wrldc.resource.adequacy.dto.response.DcAndActualSingleGenResponseDto;
import com.wrldc.resource.adequacy.entity.GeneratorDcDataEntity;
import com.wrldc.resource.adequacy.entity.GeneratorMappingEntity;

public interface GeneratorService {

	public List<String> getStateList();
	public List<GeneratorMappingEntity> getAllGenMapping() ;
	public List<DcAndActualSingleGenResponseDto> getDcAndActualDataByState(String stateName);
	public List<GeneratorDcDataEntity> getDataFromTime();

}
