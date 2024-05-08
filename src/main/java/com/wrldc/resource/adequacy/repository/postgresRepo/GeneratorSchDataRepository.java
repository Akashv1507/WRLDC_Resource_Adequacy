package com.wrldc.resource.adequacy.repository.postgresRepo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.wrldc.resource.adequacy.dto.response.GenDcResponseDto;
import com.wrldc.resource.adequacy.dto.response.GenSchResponseDto;
import com.wrldc.resource.adequacy.entity.GeneratorMappingEntity;
import com.wrldc.resource.adequacy.entity.GeneratorSchDataEntity;

public interface GeneratorSchDataRepository extends JpaRepository<GeneratorSchDataEntity, Integer> {

	GeneratorSchDataEntity findByDateTimeAndPlant(LocalDateTime currentTime, GeneratorMappingEntity plant);

	@Query(value = "SELECT  plant_name as plantName, date_time as dateTime,  sum(sch_data) as schValue FROM intraday_sch_data where plant_name =?1 and date_time >= ?2 and date_time <=?3 group by plant_name , date_time order by date_time asc ", nativeQuery = true)
	List<GenSchResponseDto> getAllDayDataByGenName(String plantName, LocalDateTime startTime,
			LocalDateTime endTime);
	
	

}
