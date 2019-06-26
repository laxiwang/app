package com.jhyx.halfroom.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TitleTypeStatus {
    HISTORY_AMATEUR(0, "历史爱好者", "http://static.half-room.com/c1a5612d96b7b8f0.png", "http://static.half-room.com/4c6f6586b2e7265a.png"),
    HISTORY_MASTER(1, "历史大咖", "http://static.half-room.com/a3543d2e145e79bb.png", "http://static.half-room.com/21ec940fa5ff1a9a.png"),
    HISTORY_MEMORY_MASTER(2, "历史记忆达人", "http://static.half-room.com/383078fcf4c5a52c.png", "http://static.half-room.com/6a712c14647c273d.png"),
    HISTORY_KING(3, "历史王者", "http://static.half-room.com/a25fb896a75a0587.png", "http://static.half-room.com/713462c31a99d05c.png"),
    POETRY_AMATEUR(4, "诗词爱好者", "http://static.half-room.com/0733eeb42282ae15.png", "http://static.half-room.com/4e952463f27cca70.png"),
    POETRY_MASTER(5, "诗词大咖", "http://static.half-room.com/00636fe0e732a6a2.png", "http://static.half-room.com/9deb8590a96404b8.png"),
    POETRY_MEMORY_MASTER(6, "诗词记忆达人", "http://static.half-room.com/72b224ba77d1e061.png", "http://static.half-room.com/1f8287a32cdeac13.png"),
    POETRY_KING(7, "诗词王者", "http://static.half-room.com/966581bfaf22007a4t.png", "http://static.half-room.com/06a74bc617b756ff.png"),
    POETRY_HISTORY_NEW_STAR(8, "文史新秀", "http://static.half-room.com/d029d27b6b621237.png", "http://static.half-room.com/4db3cd9d0187de72.png"),
    POETRY_HISTORY_MASTER(9, "文史大咖", "http://static.half-room.com/15c021f2d1ee482a.png", "http://static.half-room.com/2ad253893d703c6e.png"),
    POETRY_HISTORY_ALMIGHTY(10, "文史全能", "http://static.half-room.com/7ca7ec2d342e4f55.png", "http://static.half-room.com/051f0ca5be4dbf01.png"),
    POETRY_HISTORY_KING(11, "文史王者", "http://static.half-room.com/ed9910ec8be810c7.png", "http://static.half-room.com/b3230ab4e40102b8.png");

    private int index;
    private String msg;
    private String smallUrl;
    private String bigUrl;
}
