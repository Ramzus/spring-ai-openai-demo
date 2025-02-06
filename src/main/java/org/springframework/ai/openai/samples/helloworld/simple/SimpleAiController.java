package org.springframework.ai.openai.samples.helloworld.simple;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimpleAiController {

	private final ChatClient chatClient;

	@Autowired
	public SimpleAiController(ChatClient.Builder chatClient) {
		this.chatClient = chatClient.build();
	}

	@GetMapping("/ai/simple")
	public ResponseEntity<String> completion(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
		return ResponseEntity.ok(chatClient.prompt().user(message).call().chatResponse().getResult().getOutput().getText());
	}
}
