package com.jhyx.halfroom.serviceImpl;

import com.jhyx.halfroom.bean.CorrelationMessage;
import com.jhyx.halfroom.commons.LocalDateTimeUtil;
import com.jhyx.halfroom.commons.UniqueKeyGeneratorUtil;
import com.jhyx.halfroom.constant.CorrelationMessageStatus;
import com.jhyx.halfroom.dao.CorrelationMessageDao;
import com.jhyx.halfroom.message.DistributionMessageBean;
import com.jhyx.halfroom.service.PayMessageService;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;

@Service
@AllArgsConstructor
public class PayMessageServiceImpl implements PayMessageService, RabbitTemplate.ConfirmCallback {
    private RabbitTemplate rabbitTemplate;
    private CorrelationMessageDao correlationMessageDao;

    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(this);
    }

    @Override
    public void sendPaySuccessMessage(@NotNull DistributionMessageBean messageBean) {
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(UniqueKeyGeneratorUtil.generateUUID());
        CorrelationMessage correlationMessage = new CorrelationMessage();
        String now = LocalDateTimeUtil.getCurrentTimestamp();
        correlationMessage.setCorrelationId(correlationData.getId()).setOrderNo(messageBean.getOrderno()).setCreateTime(now).setUpdateTime(now);
        correlationMessageDao.insert_into_correlation_message(correlationMessage);
        rabbitTemplate.correlationConvertAndSend(messageBean, correlationData);
    }


    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String message) {
        if (!ack) {
            String correlationId = correlationData.getId();
            CorrelationMessage correlationMessage = correlationMessageDao.select_correlation_message_by_correlation_id(correlationId);
            correlationMessage.setType(CorrelationMessageStatus.FAILURE.getIndex());
            correlationMessageDao.update_correlation_message(correlationMessage);
        }
    }
}
