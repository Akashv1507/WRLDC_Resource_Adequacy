package com.wrldc.resource.adequacy.dto.response;

import java.util.List;

import com.wrldc.resource.adequacy.entity.TimestampDataInterface;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AdequacyPlotResponseDto {

	private List<TimestampDataInterface> demForecastDataList;
	private List<TimestampDataInterface> solarForecastDataList;
	private List<TimestampDataInterface> windForecastDataList;
	private List<TimestampDataInterface> thermalGenDataList;
	private List<TimestampDataInterface> hydroGenDataList;
	private List<TimestampDataInterface> gasGenDataList;
	private List<TimestampData> scheduleDataList;
	private Integer fullschdRevisionNo;
}
