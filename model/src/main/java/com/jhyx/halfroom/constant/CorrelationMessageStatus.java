package com.jhyx.halfroom.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CorrelationMessageStatus {
    SUCCESS(1, "消息发送成功"), FAILURE(2, "消息发送失败");
    private Integer index;
    private String msg;
}
