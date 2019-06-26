package com.jhyx.halfroom.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WeChatTokenTypeStatus {
    ACCESS_TOKEN(0, "accessToken"), JS_API_TICKET(1, "jsApiTicket");
    private Integer index;
    private String msg;
}
