package com.adeo.summit.service;


import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.function.Function;

@Service
public class WebhookCall implements Function<WebhookCall.Request, WebhookCall.Response> {

    public Response apply(Request request) {
        WebClient webClient = WebClient.create("https://webhook.site/54d6709e-4c5e-420c-9413-36b4ec9e3c46");
        webClient.post().bodyValue(request).retrieve().bodyToMono(String.class).block();

        return new Response("Ticket created for " + request.firstName() + " " + request.lastName());
    }

    public record Request(String firstName, String lastName, String problemDescription, String severity) {
    }

    public record Response(String incidentCreatedMessage) {
    }

}
