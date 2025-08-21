package com.haitao.generator.ai.model.messages;

import com.haitao.generator.enums.StreamMessageTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * AI响应的文字结果
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class AiResponseTextMessage extends StreamMessage {

    private String data;

    public AiResponseTextMessage(String data) {
        super(StreamMessageTypeEnum.AI_TEXT_RESPONSE.getValue());
        this.data = data;
    }
}
