package com.wrldc.resource.adequacy.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wrldc.resource.adequacy.entity.GeneratorDcDataEntity;
import com.wrldc.resource.adequacy.entity.GeneratorMappingEntity;

public interface GeneratorDcDataRepositoty extends JpaRepository<GeneratorDcDataEntity, Integer> {
	
	GeneratorDcDataEntity findByDateTimeAndPlant(LocalDateTime currentTime, GeneratorMappingEntity plant);
//	List<GeneratorDcDataEntity> findByDateTime(LocalDateTime dateTime);
	List<GeneratorDcDataEntity> findByPlantId( Integer id);
	
//	@Query(value="SELECT *  FROM intraday_dc_data where date_time=?1\\:\\:timestamp without time zone and plant_id=?2",nativeQuery = true)
//	GeneratorDcDataEntity getByDateTimeAndPlantId(@Param("currTime") LcurrTime, Integer id);
	
	List<GeneratorDcDataEntity> findByDateTime(LocalDateTime dateTime);

}
