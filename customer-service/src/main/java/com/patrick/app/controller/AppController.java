package com.patrick.app.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.patrick.app.service.IAppService;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Patrick Pan
 *
 */
@Slf4j
@RestController
@RequestMapping(produces = "application/json")
public class AppController {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private IAppService appService;

	@GetMapping("/questions")
	public String[] getQuestions(@RequestParam String input) {
		log.debug("=== The input is \"{}\". ===", input);
		return appService.getTopNQuestions(input);
	}

	@GetMapping("/answer")
	public String getAnswerByQuestion(@RequestParam String question) throws JsonProcessingException {
		log.debug("=== The question is \"{}\". ===", question);
		Map<String, String> map = new HashMap<>();
		map.put("answer", appService.getAnswerByQuestion(question));
		return objectMapper.writeValueAsString(map);
	}

	@PutMapping("/count")
	public void increaseCountOfQuestion(@RequestParam String question) {
		log.debug("=== The question is \"{}\". ===", question);
		appService.increaseCountOfQuestion(question);
		return;
	}
}
