package com.haitao.generator.ai.model.messages;

import com.haitao.generator.enums.StreamMessageTypeEnum;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ToolRequestMessage extends StreamMessage {

    private String id;
    private String toolName;
    private String args;

    public ToolRequestMessage(ToolExecutionRequest toolExecutionRequest) {
        super(StreamMessageTypeEnum.TOOL_REQUEST.getValue());
        this.id = toolExecutionRequest.id();
        this.toolName = toolExecutionRequest.name();
        this.args = toolExecutionRequest.arguments();
    }
}
