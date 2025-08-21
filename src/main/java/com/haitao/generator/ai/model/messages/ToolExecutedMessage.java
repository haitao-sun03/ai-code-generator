package com.haitao.generator.ai.model.messages;

import com.haitao.generator.enums.StreamMessageTypeEnum;
import dev.langchain4j.service.tool.ToolExecution;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ToolExecutedMessage extends StreamMessage {

    private String id;
    private String toolName;
    private String args;
    private String result;

    public ToolExecutedMessage(ToolExecution toolExecution) {
        super(StreamMessageTypeEnum.TOOL_EXECUTED.getValue());
        this.id = toolExecution.request().id();
        this.toolName = toolExecution.request().name();
        this.args = toolExecution.request().arguments();
        this.result = toolExecution.result();
    }
}
