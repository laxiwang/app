package com.jhyx.halfroom.constant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
public class Result<T> {
    private Integer code;
    private String msg;
    private T data;
}
