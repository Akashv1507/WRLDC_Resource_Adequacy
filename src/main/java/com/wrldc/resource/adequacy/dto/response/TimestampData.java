package com.wrldc.resource.adequacy.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TimestampData {
	private LocalDateTime timestamp;
    private double value;

}
