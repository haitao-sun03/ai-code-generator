package com.haitao.generator.enums;

import cn.hutool.core.util.ObjUtil;
import lombok.Getter;

@Getter
public enum StreamMessageTypeEnum {

    AI_TEXT_RESPONSE("AI文本类型", "ai_text_response"),
    TOOL_REQUEST("工具调用", "tool_request"),
    TOOL_EXECUTED("工具调用完成响应", "tool_executed");

    private final String text;
    private final String value;

    StreamMessageTypeEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value 枚举值的value
     * @return 枚举值
     */
    public static StreamMessageTypeEnum getEnumByValue(String value) {
        if (ObjUtil.isEmpty(value)) {
            return null;
        }
        for (StreamMessageTypeEnum anEnum : StreamMessageTypeEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

}
