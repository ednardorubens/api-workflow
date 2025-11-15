package com.example.api.workflow.model;

import com.example.api.workflow.enums.ProcessState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class OrderProcess {

    private final String id;
    private final String claimant;
    private final String definition;
    private final ProcessState state;
    private final List<ProcessHistory> history;

    private OrderProcess(String id, String claimant, String definition, ProcessState state, List<ProcessHistory> history) {
        this.id = id;
        this.claimant = claimant;
        this.definition = definition;
        this.state = state;
        this.history = history;
    }

    public static OrderProcess withDefinitionAndState(String claimant, String definition, ProcessState initialState) {
        var generatedId = UUID.randomUUID().toString();
        return new OrderProcess(generatedId, claimant, definition, initialState, new ArrayList<>());
    }

    public OrderProcess withState(ProcessState newState) {
        return new OrderProcess(id, claimant, definition, newState, history);
    }

    public OrderProcess withHistory(ProcessHistory actualHistory) {
        this.history.add(actualHistory);
        return new OrderProcess(id, claimant, definition, state, history);
    }

    public String id() {
        return id;
    }

    public String claimant() {
        return claimant;
    }

    public String definition() {
        return definition;
    }

    public ProcessState state() {
        return state;
    }

    public List<ProcessHistory> history() {
        return Collections.unmodifiableList(history);
    }

}
