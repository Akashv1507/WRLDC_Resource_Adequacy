package com.wrldc.resource.adequacy.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wrldc.resource.adequacy.entity.GeneratingUnitEntity;
import com.wrldc.resource.adequacy.repository.oracleRepo.GeneratingUnitRepository;
import com.wrldc.resource.adequacy.repository.oracleRepo.RealTimeOutageRepository;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class OracleDbDemoController {
	private GeneratingUnitRepository generatingUnitRepository;
	private RealTimeOutageRepository realTimeOutageRepository;
	@RequestMapping(value = { "/api/demo" }, method = RequestMethod.GET)
	@ResponseBody
	public String getGeneratingUnitData() {
		
		String unitNameString="UKAI  - UNIT 4";
		String currTismeString = "2024-03-12 17:00:00";
		Integer result=realTimeOutageRepository.checkUnitIsOut(unitNameString, currTismeString);
	
		return result.toString();
	}
}
