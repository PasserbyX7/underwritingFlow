// package com.shopee.demo.client;

// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.ArgumentMatchers.anyLong;
// import static org.mockito.ArgumentMatchers.anyString;
// import static org.mockito.Mockito.doAnswer;
// import static org.mockito.Mockito.doReturn;

// import java.util.HashMap;
// import java.util.Iterator;
// import java.util.Map;
// import java.util.stream.Stream;

// import javax.annotation.Resource;

// import com.shopee.demo.constant.UnderwritingTypeEnum;
// import com.shopee.demo.dao.SmeUnderwritingDAO;
// import com.shopee.demo.dao.UnderwritingContextDAO;
// import com.shopee.demo.entity.UnderwritingContextDO;
// import com.shopee.demo.service.UnderwritingContextService;
// import com.shopee.demo.service.UnderwritingFlowService;
// import com.shopee.demo.service.impl.UnderwritingContextServiceImpl;

// import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.mock.mockito.MockBean;

// @SpringBootTest
// public class SmeUnderwritingClientTest {

//     @Resource
//     UnderwritingFlowService underwritingService;

//     @InjectMocks
//     UnderwritingContextServiceImpl underwritingContextService;

//     // @Resource
//     // UnderwritingRequestFactory underwritingRequestFactory;

//     @MockBean
//     UnderwritingContextDAO underwritingContextDAO;

//     // @MockBean
//     // SmeUnderwritingDAO smeUnderwritingDAO;

//     @Test
//     void test() {
//         // given
//         Map<Long, UnderwritingContextDO> underwritingContextMap = new HashMap<>();
//         Iterator<Long> iter = Stream.iterate(0L, e -> e + 1).iterator();
//         // when
//         doAnswer(invocation -> {
//             UnderwritingContextDO entity = invocation.getArgument(0);
//             if (entity.getId() == null) {
//                 entity.setId(iter.next());
//             }
//             underwritingContextMap.put(entity.getId(), entity);
//             return entity;
//         })
//                 .when(underwritingContextDAO)
//                 .saveOrUpdateById(any(UnderwritingContextDO.class));
//         doAnswer(invocation -> underwritingContextMap.get(invocation.getArgument(0)))
//                 .when(underwritingContextDAO)
//                 .selectByPrimaryKey(anyLong());
//         // UnderwritingRequest request = underwritingRequestFactory.create(UnderwritingTypeEnum.SME, "underwritingId");
//         long ctxId = underwritingService.createUnderwritingTask(request);
//         underwritingService.executeUnderwritingTask(ctxId);
//     }

// }
