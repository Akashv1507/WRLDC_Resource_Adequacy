package com.wrldc.resource.adequacy.dto.response.wbesResponse;

import java.util.List;

import com.wrldc.resource.adequacy.dto.response.TimestampData;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WbesDataResp {

	private List<TimestampData> wbesSdlDataList;
	private Integer fullschdRevisionNo;
}
