package com.wrldc.resource.adequacy.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.wrldc.resource.adequacy.dto.response.TimestampData;
import com.wrldc.resource.adequacy.service.ScadaApiService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class ScadaApiController {

	
    private ScadaApiService scadaApiService;

    @GetMapping("/{measId}/{startDtStr}/{endDtStr}")
    public List<TimestampData> fetchData(
            @PathVariable String measId,
            @PathVariable String startDtStr,
            @PathVariable String endDtStr
    ) {
        return scadaApiService.fetchData(measId, startDtStr, endDtStr);
    }
}
