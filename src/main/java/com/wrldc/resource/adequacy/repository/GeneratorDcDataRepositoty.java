package com.wrldc.resource.adequacy.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.wrldc.resource.adequacy.dto.response.GenDcResponseDto;
import com.wrldc.resource.adequacy.entity.GeneratorDcDataEntity;
import com.wrldc.resource.adequacy.entity.GeneratorMappingEntity;

public interface GeneratorDcDataRepositoty extends JpaRepository<GeneratorDcDataEntity, Integer> {

	GeneratorDcDataEntity findByDateTimeAndPlant(LocalDateTime currentTime, GeneratorMappingEntity plant);

	@Query(value = "SELECT  plant_name as plantName, date_time as dateTime,  sum(dc_data) as dcValue FROM intraday_dc_data where plant_name =?1 and date_time >= ?2 and date_time <=?3 group by plant_name , date_time order by date_time asc ", nativeQuery = true)
	List<GenDcResponseDto> getAllDayDataByGenName(String plantName, LocalDateTime startTime,
			LocalDateTime endTime);
	
	

}
