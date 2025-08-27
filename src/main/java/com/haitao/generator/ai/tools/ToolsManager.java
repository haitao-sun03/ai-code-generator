package com.haitao.generator.ai.tools;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Getter
public class ToolsManager {

    private BaseTool[] tools;

    private Map<String, BaseTool> toolsMap = new HashMap<>();

    public ToolsManager(BaseTool[] tools) {
        this.tools = tools;
        for (BaseTool tool : tools) {
            toolsMap.put(tool.toolName(), tool);
        }
    }

    public BaseTool getTool(String toolName) {
        return toolsMap.get(toolName);
    }

}
