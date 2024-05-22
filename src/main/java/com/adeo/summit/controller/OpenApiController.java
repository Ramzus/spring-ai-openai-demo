package com.adeo.summit.controller;

import com.adeo.summit.service.OpenApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OpenApiController {

	private final OpenApiService openApiService;

	@Autowired
	public OpenApiController(OpenApiService openApiService) {
		this.openApiService = openApiService;
	}

	@GetMapping("/ai/call")
	public ResponseEntity<String> completion(@RequestParam(value = "message") String message) {
		return ResponseEntity.ok(openApiService.call(message));
	}
}
