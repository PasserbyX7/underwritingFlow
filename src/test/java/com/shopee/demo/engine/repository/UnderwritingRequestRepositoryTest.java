package com.shopee.demo.engine.repository;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.shopee.demo.engine.constant.UnderwritingTypeEnum;
import com.shopee.demo.engine.repository.impl.UnderwritingRequestRepositoryImpl;
import com.shopee.demo.engine.type.request.SmeUnderwritingRequest;
import com.shopee.demo.infrastructure.dal.dao.SmeUnderwritingDAO;
import com.shopee.demo.infrastructure.dal.data.SmeUnderwritingDO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@DisplayName("UnderwritingRequestRepositoryTest")
@MockitoSettings(strictness = Strictness.LENIENT)
public class UnderwritingRequestRepositoryTest {

    @InjectMocks
    private UnderwritingRequestRepositoryImpl underwritingRequestRepository;

    @Mock
    private SmeUnderwritingDAO smeUnderwritingDAO;

    @Mock
    private SmeUnderwritingRequest underwritingRequest;

    @Test
    void testFindSuccess() {
        // given
        mockStatic(SmeUnderwritingRequest.class);
        // when
        underwritingRequestRepository.find("underwritingId", UnderwritingTypeEnum.SME);
        // then
        verify(smeUnderwritingDAO, times(1)).selectByUnderwritingId(anyString());
    }

    @Test
    void testFind() {
        // then
        assertThrows(IllegalArgumentException.class,
                () -> underwritingRequestRepository.find("underwritingId", UnderwritingTypeEnum.RETAIL));
    }

    @Test
    void testSaveSuccess() {
        // given
        mockStatic(SmeUnderwritingDO.class);
        doReturn(UnderwritingTypeEnum.SME).when(underwritingRequest).getUnderwritingType();
        // when
        underwritingRequestRepository.save(underwritingRequest);
        // then
        verify(smeUnderwritingDAO, times(1)).insertSelective(any());
    }

    @Test
    void testSaveFail() {
        // given
        doReturn(UnderwritingTypeEnum.RETAIL).when(underwritingRequest).getUnderwritingType();
        // then
        assertThrows(IllegalArgumentException.class,
                () -> underwritingRequestRepository.save(underwritingRequest));
    }

}
