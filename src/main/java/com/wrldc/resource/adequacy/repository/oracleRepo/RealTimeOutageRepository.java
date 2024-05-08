package com.wrldc.resource.adequacy.repository.oracleRepo;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.wrldc.resource.adequacy.entity.RealTimeOutageEntity;

public interface RealTimeOutageRepository extends JpaRepository<RealTimeOutageEntity, Integer>{
	@Query(value = "SELECT Count(*) AS Is_out  FROM REPORTING_WEB_UI_UAT.real_time_outage  rto WHERE rto.ELEMENTNAME =?1 AND (TO_CHAR(rto.outage_date, 'YYYY-MM-DD') || ' ' || rto.OUTAGE_TIME) <= ?2 AND ((rto.REVIVED_DATE IS NULL) OR (TO_CHAR(rto.REVIVED_DATE, 'YYYY-MM-DD') || ' ' || rto.REVIVED_TIME) >= ?2)", nativeQuery = true)
	Integer checkUnitIsOut(String unitName, String currTimeStr);
}
