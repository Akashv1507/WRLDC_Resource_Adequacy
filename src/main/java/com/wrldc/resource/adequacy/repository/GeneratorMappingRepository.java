package com.wrldc.resource.adequacy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.wrldc.resource.adequacy.entity.GeneratorDcDataEntity;
import com.wrldc.resource.adequacy.entity.GeneratorMappingEntity;

@Repository
public interface GeneratorMappingRepository extends JpaRepository<GeneratorMappingEntity, Integer> {

	@Query(value="SELECT DISTINCT State FROM mapping_table",nativeQuery = true)
	List<String> findDistinctState();
	
	List<GeneratorMappingEntity> findByState(String stateName);
}
