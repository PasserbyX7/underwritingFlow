// package com.shopee.demo.domain;

// import com.shopee.demo.constant.UnderwritingTypeEnum;
// import com.shopee.demo.entity.BaseUnderwritingDO;
// import com.shopee.demo.flow.Flow;

// import org.springframework.stereotype.Component;

// import lombok.NoArgsConstructor;


// @Component
// @NoArgsConstructor
// public class RetailUnderwritingRequest extends AbstractUnderwritingRequest {

//     public RetailUnderwritingRequest(BaseUnderwritingDO underwritingDO){
//         super(underwritingDO);
//     }

//     @Override
//     public Flow getFlow() {
//         return applicationContext.getBean("retailFlow", Flow.class);
//     }

//     @Override
//     public UnderwritingTypeEnum getUnderwritingType() {
//         return UnderwritingTypeEnum.RETAIL;
//     }

// }
