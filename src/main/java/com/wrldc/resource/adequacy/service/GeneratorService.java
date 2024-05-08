package com.wrldc.resource.adequacy.service;


import java.util.List;
import com.wrldc.resource.adequacy.dto.response.DcSchActualSingleGenResponseDto;
import com.wrldc.resource.adequacy.dto.response.GenDcResponseDto;
import com.wrldc.resource.adequacy.dto.response.GenDcSchResponseDto;
import com.wrldc.resource.adequacy.entity.GeneratorDcDataEntity;
import com.wrldc.resource.adequacy.entity.GeneratorMappingEntity;

public interface GeneratorService {

	public List<String> getStateList();
	public List<DcSchActualSingleGenResponseDto> getDcSchActualDataByState(String stateName);
	public List<GenDcSchResponseDto> getDcSchByGen(String genName);
	

}
