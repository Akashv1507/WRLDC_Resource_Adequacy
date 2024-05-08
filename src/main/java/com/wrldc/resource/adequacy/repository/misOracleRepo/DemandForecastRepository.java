package com.wrldc.resource.adequacy.repository.misOracleRepo;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.wrldc.resource.adequacy.entity.DemandForecastEntity;
import com.wrldc.resource.adequacy.entity.TimestampDataInterface;

public interface DemandForecastRepository extends JpaRepository<DemandForecastEntity, Integer> {
	
	//this query will be used when you use starttime and endTime as String , in query write TO_DATE()
//	@Query(value = "SELECT DF.TIME_STAMP AS timestamp, DF.FORECASTED_DEMAND_VALUE as value FROM MIS_WAREHOUSE.DAYAHEAD_DEMAND_FORECAST DF WHERE DF.ENTITY_TAG=?1 AND DF.TIME_STAMP>=TO_DATE(?2,'YYYY-MM-DD HH24:MI:SS') AND DF.TIME_STAMP<=TO_DATE(?3,'YYYY-MM-DD HH24:MI:SS')", nativeQuery = true)
//	List<TimestampDataInterface> getDemandForecast(String stateScadaId, String startTime, String endTime);
	
	//this query will be used when you use starttime and endTime as LocalDateTime 
	@Query(value = "SELECT DF.TIME_STAMP AS timestamp, DF.FORECASTED_DEMAND_VALUE as value FROM MIS_WAREHOUSE.DAYAHEAD_DEMAND_FORECAST DF WHERE DF.ENTITY_TAG=?1 AND DF.TIME_STAMP>=?2 AND DF.TIME_STAMP<=?3 order by  DF.TIME_STAMP", nativeQuery = true)
	List<TimestampDataInterface> getDemandForecast(String stateScadaId, LocalDateTime startTime, LocalDateTime endTime);
}
