// package com.shopee.demo.domain;

// import com.shopee.demo.entity.BaseUnderwritingDO;

// import org.springframework.beans.BeansException;
// import org.springframework.context.ApplicationContext;
// import org.springframework.context.ApplicationContextAware;

// import lombok.AllArgsConstructor;
// import lombok.NoArgsConstructor;

// @NoArgsConstructor
// @AllArgsConstructor
// public abstract class AbstractUnderwritingRequest implements UnderwritingRequest, ApplicationContextAware {

//     protected static ApplicationContext applicationContext;

//     protected BaseUnderwritingDO underwritingDO;

//     @Override
//     public void setApplicationContext(ApplicationContext context) throws BeansException {
//         applicationContext = context;
//     }

//     @Override
//     public final BaseUnderwritingDO getUnderwritingData() {
//         return underwritingDO;
//     }

//     @Override
//     public final String getUnderwritingId() {
//         return underwritingDO.getUnderwritingId();
//     }

//     @Override
//     public final long getRequestTime() {
//         return underwritingDO.getRequestTime();
//     }

//     @Override
//     public final long getRequestExpireTime() {
//         return underwritingDO.getRequestExpireTime();
//     }

// }
