package com.wrldc.resource.adequacy.repository.postgresRepo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.wrldc.resource.adequacy.dto.response.GenDcResponseDto;
import com.wrldc.resource.adequacy.entity.GeneratorDcDataEntity;
import com.wrldc.resource.adequacy.entity.GeneratorMappingEntity;
import com.wrldc.resource.adequacy.entity.TimestampDataInterface;

public interface GeneratorDcDataRepository extends JpaRepository<GeneratorDcDataEntity, Integer> {

	GeneratorDcDataEntity findByDateTimeAndPlant(LocalDateTime currentTime, GeneratorMappingEntity plant);

	@Query(value = "SELECT  plant_name as plantName, date_time as dateTime,  sum(dc_data) as dcValue FROM intraday_dc_data where plant_name =?1 and date_time >= ?2 and date_time <=?3 group by plant_name , date_time order by date_time asc ", nativeQuery = true)
	List<GenDcResponseDto> getAllDayDataByGenName(String plantName, LocalDateTime startTime,
			LocalDateTime endTime);
	
	@Query(value = "select\r\n"
			+ "	idd.date_time as timestamp,\r\n"
			+ "	sum(idd.dc_data) as value\r\n"
			+ "from\r\n"
			+ "	intraday_dc_data idd ,\r\n"
			+ "	mapping_table mt\r\n"
			+ "where\r\n"
			+ "	idd.plant_id = mt.id\r\n"
			+ "	and idd.date_time >= ?3\r\n"
			+ "	and idd.date_time <= ?4\r\n"
			+ "	and mt.state = ?1\r\n"
			+ "	and mt.fuel_type = ?2\r\n"
			+ "group by\r\n"
			+ "	idd.date_time\r\n"
			+ "order by\r\n"
			+ "	timestamp", nativeQuery = true)
	List<TimestampDataInterface> geSumDcDataState(String stateName, String fuelType, LocalDateTime startTime,
			LocalDateTime endTime);
	

}
