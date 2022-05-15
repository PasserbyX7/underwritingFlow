package com.shopee.demo.engine.integration;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import com.shopee.demo.app.service.UnderwritingService;
import com.shopee.demo.engine.type.request.SmeUnderwritingRequest;
import com.shopee.demo.engine.type.request.UnderwritingRequest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UnderwritingServiceTest {

    @Resource
    private UnderwritingService underwritingService;

    @Test
    void testExecuteUnderwriting() throws Exception {
        UnderwritingRequest request = mockUnderwritingRequest();
        underwritingService.executeUnderwriting(request);
        TimeUnit.SECONDS.sleep(5);
    }

    private UnderwritingRequest mockUnderwritingRequest() {
        return new SmeUnderwritingRequest("underwritingId", 1652569702679L, 1652669702679L, "smeData");
    }

}
