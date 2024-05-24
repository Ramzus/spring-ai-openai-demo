package com.adeo.summit.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ContextStore {

    Map<String, List<String>> contextStore = new ConcurrentHashMap<>();

    public void addContext(String contextId, List<String> values) {
        if(contextStore.containsKey(contextId)) {
            contextStore.get(contextId).addAll(values);
        } else {
            contextStore.put(contextId, new ArrayList<>(values));
        }
    }

    public List<String> retrieveContext(String contextId) {
        return contextStore.get(contextId) != null ? contextStore.get(contextId) : List.of();
    }

}
