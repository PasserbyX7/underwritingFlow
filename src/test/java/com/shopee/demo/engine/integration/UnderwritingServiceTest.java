package com.shopee.demo.engine.integration;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import com.shopee.demo.app.service.UnderwritingService;
import com.shopee.demo.engine.type.request.SmeUnderwritingRequest;
import com.shopee.demo.engine.type.request.UnderwritingRequest;
import com.shopee.demo.infrastructure.dal.dao.SmeUnderwritingDAO;
import com.shopee.demo.infrastructure.dal.data.SmeUnderwritingDO;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class UnderwritingServiceTest {

    @Resource
    private UnderwritingService underwritingService;

    @MockBean
    private SmeUnderwritingDAO smeUnderwritingDAO;

    @Test
    void testExecuteUnderwriting() throws Exception {
        doReturn(Optional.empty(), Optional.ofNullable(mockSmeUnderwritingDO()))
                .when(smeUnderwritingDAO)
                .selectByUnderwritingId(anyString());
        UnderwritingRequest request = mockUnderwritingRequest();
        underwritingService.executeUnderwriting(request);
        TimeUnit.SECONDS.sleep(5);
    }

    private UnderwritingRequest mockUnderwritingRequest() {
        return new SmeUnderwritingRequest("underwritingId", 1652569702679L, 1652669702679L, "smeData");
    }

    private SmeUnderwritingDO mockSmeUnderwritingDO() {
        SmeUnderwritingDO smeUnderwritingDO = new SmeUnderwritingDO();
        smeUnderwritingDO.setId(1L);
        smeUnderwritingDO.setUnderwritingId("underwritingId");
        smeUnderwritingDO.setRequestTime(1652569702679L);
        smeUnderwritingDO.setRequestExpireTime(1652669702679L);
        smeUnderwritingDO.setSmeData("smeData");
        return smeUnderwritingDO;
    }
}
