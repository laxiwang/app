package com.jhyx.halfroom.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {
    SUCCESS(0, "success"), ERROR(1, "未知异常"), USER_NOT_EXITS(2, "用户不存在"),
    PHONE_CODE_ERROR(3, "验证码错误"), PHONE_CODE_EXPIRED(4, "验证码过期"), USER_NOT_LOGIN(5, "用户未登录"),
    INTEGRATION_DEFICIENCY(6, "积分不足"), USER_ORDER_NOT_EXITS(7, "用户订单不存在"), USER_ORDER_PAY_FAIL(8, "支付失败"),
    INTERFACE_NOT_OPEN(9, "接口未开放"), NO_PERMISSIONS(10, "用户没有权限"), MEDIA_NOT_EXITS(11, "音视频文件不存在"),
    BOOK_CHAPTER_PART_CONTENT_NOT_EXITS(12, "文稿不存在"), USER_HAS_PURCHASE_BOOK(13, "用户已经购买了该课程"),
    CARD_NOT_EXITS(14, "会员卡不存在"), CARD_NOT_ACTIVATE(15, "会员卡未激活"), CARD_HAS_USED(16, "会员卡已经使用"),
    BRANCH_INVITE_RECORD_NOT_EXITS(17, "分会邀请记录不存在"), HAS_USED_EXPERIENCE_CARD(18, "半月卡仅限使用一次，不可多次使用"),
    VIRTUAL_POINT_DEFICIENCY(19, "虚拟货币不足，请充值"), CARD_FEE_NOT_MATCH_BOOK_PRICE(20, "课程价格和实体卡的价格不一致"),
    ARGS_ERROR(21, "参数错误"), USER_HAD_TEAM(22, "您已经创建或加入战队了哦"), USER_NOT_QUALIFICATION(23, "暂无资格领取"),
    USER_HAS_RECEIVE_GIFT(24, "已经领取了该礼包，不能重复领取");
    private Integer index;
    private String msg;
}
