package com.example.api.workflow.repository;

import com.example.api.workflow.model.OrderProcess;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ProcessRepository {

    private static final ConcurrentHashMap<String, OrderProcess> processStore = new ConcurrentHashMap<>();

    public OrderProcess save(OrderProcess process) {
        processStore.put(process.id(), process);
        return process;
    }

    public Optional<OrderProcess> findById(String processId) {
        return Optional.ofNullable(processId)
                .filter(StringUtils::isNotBlank)
                .map(processStore::get);
    }

}
