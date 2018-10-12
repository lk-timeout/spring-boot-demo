package com.timeout.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class HelloController {

	@RequestMapping("/hellos")
	public String test() {
		log.info("1235465");
		return "helle world";
	}
}
