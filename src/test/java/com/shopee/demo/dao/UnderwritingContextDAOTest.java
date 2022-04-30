package com.shopee.demo.dao;

import java.util.EnumMap;
import java.util.Map;

import javax.annotation.Resource;

import com.shopee.demo.constant.StrategyEnum;
import com.shopee.demo.dto.SmeStrategy1InputDTO;
import com.shopee.demo.entity.UnderwritingContextDO;
import com.shopee.demo.strategy.StrategyInput;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UnderwritingContextDAOTest {

    @Resource
    UnderwritingContextDAO underwritingContextDAO;

    @Test
    void testInsertSelective() {
        UnderwritingContextDO underwritingContextDO = new UnderwritingContextDO();
        Map<StrategyEnum, StrategyInput> strategyInput = new EnumMap<>(StrategyEnum.class);
        strategyInput.put(StrategyEnum.SME_STRATEGY_1, new SmeStrategy1InputDTO("token"));
        underwritingContextDO.setStrategyInput(strategyInput);
        underwritingContextDO.setCurrentStrategy(StrategyEnum.SME_STRATEGY_1);
        underwritingContextDAO.insertSelective(underwritingContextDO);
    }

    @Test
    void testSelectByPrimaryKey() {
        UnderwritingContextDO underwritingContext = underwritingContextDAO.selectByPrimaryKey(5L);
        System.out.println(underwritingContext);
        System.out.println(underwritingContext.getStrategyInput().get(StrategyEnum.SME_STRATEGY_1).getClass());
    }

}
