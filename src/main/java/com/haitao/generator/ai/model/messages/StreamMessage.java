package com.haitao.generator.ai.model.messages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 流式消息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StreamMessage {

    private String type;
}
