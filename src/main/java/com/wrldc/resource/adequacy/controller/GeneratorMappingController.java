package com.wrldc.resource.adequacy.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wrldc.resource.adequacy.dto.ResponseHandler;
import com.wrldc.resource.adequacy.entity.GeneratorMappingEntity;
import com.wrldc.resource.adequacy.service.GeneratorService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
public class GeneratorMappingController {

	private GeneratorService generatorMappingService;
	
	
	@GetMapping("/state-list")
	public ResponseEntity<Object> getStateList()
	{
		List<String> allStateList= generatorMappingService.getStateList();
		if(allStateList.isEmpty()==true) {
			return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "No state Found! Try Again", null);
		}
		else {
			return ResponseHandler.generateResponse(HttpStatus.OK, true, " State Name List Fetch Successfull", allStateList);

		}
		
	}
}
