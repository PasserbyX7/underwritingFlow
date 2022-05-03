package com.shopee.demo.flow;

import javax.annotation.PostConstruct;

import com.shopee.demo.constant.StrategyEnum;
import com.shopee.demo.constant.UnderwritingTypeEnum;
import com.shopee.demo.domain.type.request.UnderwritingRequest;
import com.shopee.demo.strategy.Strategy;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public abstract class StrategyFlow<T extends UnderwritingRequest> implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public abstract Strategy<T> getFirstStrategy();

    @PostConstruct
    protected abstract void configStrategyChain();

    public final Strategy<T> getStrategy(StrategyEnum strategyName) {
        Strategy<T> head = getFirstStrategy();
        while (head != null) {
            if (head.getStrategyName() == strategyName) {
                return head;
            }
            head = head.getNextStrategy();
        }
        return null;
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext=context;
    }

    public static <T extends UnderwritingRequest> StrategyFlow<T> of(UnderwritingTypeEnum type){
        String strategyFlowName=type.toString().toLowerCase()+"StrategyFlow";
        return applicationContext.getBean(strategyFlowName,StrategyFlow.class);
    }

    // public final void execute(UnderwritingContext underwritingContext) {
    // // while (!underwritingContext.isTerminal()) {
    // //
    // getFlowStatus(underwritingContext.getFlowStatus()).execute(underwritingContext);
    // // }
    // }

    // protected abstract void onFlowPending();

    // protected abstract void onFlowApproved();

    // protected abstract void onFlowReject();

    // protected abstract void onFlowCancelled();

    // private FlowStatus getFlowStatus(UnderwritingFlowStatusEnum status) {
    // switch (status) {
    // case CREATED:
    // return new CreatedFlowStatus();
    // case ONGOING:
    // return new OngoingFlowStatus();
    // case PENDING:
    // return new PendingFlowStatus();
    // case APPROVED:
    // return new ApprovedFlowStatus();
    // case REJECT:
    // return new RejectFlowStatus();
    // case CANCELLED:
    // return new CancelledFlowStatus();
    // default:
    // return null;
    // }
    // }

    // private class CreatedFlowStatus implements FlowStatus {

    // @Override
    // public void execute(UnderwritingContext underwritingContext) {
    // underwritingContext.setFlowStatus(getFlowStatus());
    // // underwritingContext.setTerminal(getFlowStatus().isTerminal());
    // contextService.save(underwritingContext);
    // underwritingContext.setFlowStatus(UnderwritingFlowStatusEnum.ONGOING);
    // }

    // @Override
    // public UnderwritingFlowStatusEnum getFlowStatus() {
    // return UnderwritingFlowStatusEnum.CREATED;
    // }

    // }

    // private class OngoingFlowStatus implements FlowStatus {

    // @Override
    // public void execute(UnderwritingContext underwritingContext) {
    // // 执行策略
    // Strategy currentStrategy = underwritingContext.getCurrentStrategy();
    // // StrategyResult res =
    // currentStrategy.execute(underwritingContext.getStrategyContext());
    // // if (res.getStatus() == StrategyStatusEnum.REJECT) {
    // // underwritingContext.setFlowStatus(UnderwritingFlowStatusEnum.REJECT);
    // // return;
    // // }
    // // if (res.getStatus() == StrategyStatusEnum.SUSPEND) {
    // // underwritingContext.setFlowStatus(UnderwritingFlowStatusEnum.PENDING);
    // // return;
    // // }
    // // if (res.getStatus() == StrategyStatusEnum.TERMINAL) {
    // // underwritingContext.setFlowStatus(UnderwritingFlowStatusEnum.CANCELLED);
    // // return;
    // // }
    // // if (res.getStatus() == StrategyStatusEnum.PASS &&
    // currentStrategy.getNextStrategy() == null) {
    // // underwritingContext.setFlowStatus(UnderwritingFlowStatusEnum.APPROVED);
    // // return;
    // // }
    // underwritingContext.setFlowStatus(getFlowStatus());
    // //
    // underwritingContext.setCurrentStrategy(currentStrategy.getNextStrategy().getStrategyName());
    // // underwritingContext.setTerminal(getFlowStatus().isTerminal());
    // contextService.save(underwritingContext);
    // }

    // @Override
    // public UnderwritingFlowStatusEnum getFlowStatus() {
    // return UnderwritingFlowStatusEnum.ONGOING;
    // }

    // }

    // private class ApprovedFlowStatus implements FlowStatus {

    // @Override
    // public void execute(UnderwritingContext underwritingContext) {
    // underwritingContext.setFlowStatus(getFlowStatus());
    // // underwritingContext.setTerminal(getFlowStatus().isTerminal());
    // contextService.save(underwritingContext);
    // onFlowApproved();
    // }

    // @Override
    // public UnderwritingFlowStatusEnum getFlowStatus() {
    // return UnderwritingFlowStatusEnum.APPROVED;
    // }

    // }

    // private class PendingFlowStatus implements FlowStatus {

    // @Override
    // public void execute(UnderwritingContext underwritingContext) {
    // //
    // underwritingContext.setSuspendReason(res.getSuspendDataSource().toString());
    // underwritingContext.setFlowStatus(getFlowStatus());
    // // underwritingContext.setTerminal(getFlowStatus().isTerminal());
    // contextService.save(underwritingContext);
    // onFlowPending();
    // }

    // @Override
    // public UnderwritingFlowStatusEnum getFlowStatus() {
    // return UnderwritingFlowStatusEnum.PENDING;
    // }

    // }

    // private class RejectFlowStatus implements FlowStatus {

    // @Override
    // public void execute(UnderwritingContext underwritingContext) {
    // underwritingContext.setFlowStatus(getFlowStatus());
    // // underwritingContext.setTerminal(getFlowStatus().isTerminal());
    // contextService.save(underwritingContext);
    // onFlowReject();
    // }

    // @Override
    // public UnderwritingFlowStatusEnum getFlowStatus() {
    // return UnderwritingFlowStatusEnum.REJECT;
    // }

    // }

    // private class CancelledFlowStatus implements FlowStatus {

    // @Override
    // public void execute(UnderwritingContext underwritingContext) {
    // // underwritingContext.setTerminalReason(res.getTerminalReason().toString());
    // underwritingContext.setFlowStatus(getFlowStatus());
    // // underwritingContext.setTerminal(getFlowStatus().isTerminal());
    // contextService.save(underwritingContext);
    // onFlowCancelled();
    // }

    // @Override
    // public UnderwritingFlowStatusEnum getFlowStatus() {
    // return UnderwritingFlowStatusEnum.CANCELLED;
    // }

    // }

}
