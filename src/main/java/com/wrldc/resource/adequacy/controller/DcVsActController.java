package com.wrldc.resource.adequacy.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wrldc.resource.adequacy.dto.ResponseHandler;
import com.wrldc.resource.adequacy.dto.response.DcSchActualSingleGenResponseDto;
import com.wrldc.resource.adequacy.dto.response.GenDcSchResponseDto;
import com.wrldc.resource.adequacy.service.GeneratorService;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class DcVsActController {

	private GeneratorService generatorMappingService;

	@RequestMapping(value = { "/home/dcVsAct" }, method = RequestMethod.GET)
	public String getDcVsActPage(Model model) {
		List<String> statesList = generatorMappingService.getStateList();
		model.addAttribute("statesList", statesList);
		return "dcSchAct";
	}

	@RequestMapping(value = { "/api/dcVsAct/{stateName}" }, method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Object> getDcAndActualDataByState(@PathVariable String stateName) {
		List<DcSchActualSingleGenResponseDto> dcAndActualResponseDto = generatorMappingService.getDcSchActualDataByState(stateName);
		if (dcAndActualResponseDto.isEmpty() == true) {
			return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "DC and Actual Data Fetch UnSuccessfull",
					null);
		} else {
			return ResponseHandler.generateResponse(HttpStatus.OK, true, "DC and Actual Data Fetch Successfull",
					dcAndActualResponseDto);
		}

	}
	
	@RequestMapping(value = { "/api/dcSch/{genName}" }, method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Object> getDcSchByGenName(@PathVariable String genName) {
		List<GenDcSchResponseDto>  genDcSchResponseDtoList= generatorMappingService.getDcSchByGen(genName);
		if (genDcSchResponseDtoList.isEmpty() == true || (genDcSchResponseDtoList==null)==true) {
			return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "DC Data Fetch UnSuccessfull",
					null);
		} else {
			return ResponseHandler.generateResponse(HttpStatus.OK, true, "DC Data Fetch Successfull",
					genDcSchResponseDtoList);
		}

	}
	
	
		

	
}
