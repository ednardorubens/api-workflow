package com.example.api.workflow.repository;

import com.example.api.workflow.enums.ProcessState;
import com.example.api.workflow.model.OrderProcess;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProcessRepositoryTest {

    private ProcessRepository processRepository = new ProcessRepository();

    @Test
    void testFindById() {
        var orderProcess = OrderProcess.withDefinitionAndState("claimant-1", "my-definition", ProcessState.STARTED);
        processRepository.save(orderProcess);

        var orderProcessReceived = processRepository.findById(orderProcess.id()).get();

        assertEquals(orderProcess.id(), orderProcessReceived.id());
        assertEquals(orderProcess.claimant(), orderProcessReceived.claimant());
        assertEquals(orderProcess.definition(), orderProcessReceived.definition());
        assertEquals(orderProcess.state(), orderProcessReceived.state());
    }

}
