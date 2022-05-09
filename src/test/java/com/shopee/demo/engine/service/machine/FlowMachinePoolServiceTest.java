package com.shopee.demo.engine.service.machine;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.shopee.demo.engine.entity.machine.FlowStateMachine;
import com.shopee.demo.engine.service.machine.impl.FlowStateMachinePoolServiceImpl;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@DisplayName("FlowMachinePoolServiceTest")
@MockitoSettings(strictness = Strictness.LENIENT)
public class FlowMachinePoolServiceTest {

    @Mock
    private GenericObjectPool<FlowStateMachine> flowMachinePool;

    @Mock
    private FlowStateMachinePersistService flowStateMachinePersistService;

    @Mock
    private FlowStateMachine flowStateMachine;

    @InjectMocks
    private FlowStateMachinePoolServiceImpl flowStateMachinePoolService;

    @Test
    void testAcquire() throws Exception {
        // given
        long underwritingFlowId = 1L;
        doReturn(flowStateMachine)
                .when(flowMachinePool)
                .borrowObject();
        // when
        flowStateMachinePoolService.acquire(underwritingFlowId);
        // then
        verify(flowMachinePool, times(1)).borrowObject();
        verify(flowStateMachinePersistService, times(1)).restore(any(), anyLong());
        verify(flowStateMachine, times(1)).start();
    }

    @Test
    void testRelease() {
        // when
        flowStateMachinePoolService.release(flowStateMachine);
        // then
        verify(flowMachinePool, times(1)).returnObject(any());
    }

}