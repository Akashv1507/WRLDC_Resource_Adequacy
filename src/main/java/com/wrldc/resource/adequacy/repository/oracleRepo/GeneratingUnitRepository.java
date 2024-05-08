package com.wrldc.resource.adequacy.repository.oracleRepo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.wrldc.resource.adequacy.entity.GeneratingUnitEntity;

public interface GeneratingUnitRepository extends JpaRepository<GeneratingUnitEntity, Integer> {
	@Query(value = "SELECT  ID, UNIT_NAME, INSTALLED_CAPACITY FROM REPORTING_WEB_UI_UAT.GENERATING_UNIT gu WHERE gu.UNIT_NAME=?1  ", nativeQuery = true)
	GeneratingUnitEntity getUnitInfo(String unitName);
}
