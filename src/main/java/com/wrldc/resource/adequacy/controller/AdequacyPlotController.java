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
import com.wrldc.resource.adequacy.dto.response.AdequacyPlotResponseDto;
import com.wrldc.resource.adequacy.service.AdequacyPlotsService;
import com.wrldc.resource.adequacy.service.GeneratorService;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class AdequacyPlotController {

	private GeneratorService generatorMappingService;
	private AdequacyPlotsService adequacyPlotsService;

	@RequestMapping(value = { "/home/adequacyPlots" }, method = RequestMethod.GET)
	public String getAdequacyPlotPage(Model model) {
		List<String> statesList = generatorMappingService.getStateList();
		model.addAttribute("statesList", statesList);
		return "adequacyPlot";
	}
	
	@RequestMapping(value = { "/api/adequacyPlotData/{stateScadaId}/{startTimeStr}/{endTimeStr}" }, method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Object> getAdequacyPlotData(@PathVariable String stateScadaId, @PathVariable String startTimeStr, @PathVariable String endTimeStr) {
		AdequacyPlotResponseDto adequacyPlotDataResponseDto = adequacyPlotsService.getAdequacyPlotData(stateScadaId, startTimeStr, endTimeStr);
		return ResponseHandler.generateResponse(HttpStatus.OK, true, "Adequacy Plot Data Fetch Successful",
				adequacyPlotDataResponseDto);
		
		
	}

}
