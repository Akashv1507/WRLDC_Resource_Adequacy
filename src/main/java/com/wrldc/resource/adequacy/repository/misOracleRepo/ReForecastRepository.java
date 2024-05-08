package com.wrldc.resource.adequacy.repository.misOracleRepo;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.wrldc.resource.adequacy.entity.ReForecastEntity;
import com.wrldc.resource.adequacy.entity.TimestampDataInterface;

public interface ReForecastRepository extends JpaRepository<ReForecastEntity, Integer> {
	
	//this query will be used when you use starttime and endTime as String , in query write TO_DATE()
//	@Query(value = "SELECT RF.TIME_STAMP AS timestamp, RF.RE_FORECASTED_VALUE as value FROM MIS_WAREHOUSE.RE_FORECAST RF WHERE RF.ENTITY_TAG=?1 AND RF.RE_TYPE=?2 AND RF.TIME_STAMP>=TO_DATE(?3,'YYYY-MM-DD HH24:MI:SS') AND RF.TIME_STAMP<=TO_DATE(?4,'YYYY-MM-DD HH24:MI:SS')", nativeQuery = true)
//	List<TimestampDataInterface> getReForecast(String stateScadaId, String reType, String startTime, String endTime);
	
	//this query will be used when you use starttime and endTime as LocalDateTime 
	@Query(value = "SELECT RF.TIME_STAMP AS timestamp, RF.RE_FORECASTED_VALUE as value FROM MIS_WAREHOUSE.RE_FORECAST RF WHERE RF.ENTITY_TAG=?1 AND RF.RE_TYPE=?2 AND RF.TIME_STAMP>=?3 AND RF.TIME_STAMP<=?4 order by RF.TIME_STAMP", nativeQuery = true)
	List<TimestampDataInterface> getReForecast(String stateScadaId, String reType, LocalDateTime startTime, LocalDateTime endTime);
}
