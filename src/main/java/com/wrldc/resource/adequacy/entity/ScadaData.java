package com.wrldc.resource.adequacy.entity;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ScadaData {
	private LocalDateTime timestamp;
    private double value;

}
