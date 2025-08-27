package com.haitao.generator.ai.tools;


import cn.hutool.json.JSONObject;

/**
 * 工具基类，为了方便扩展
 */
public abstract class BaseTool {

    public abstract String toolName();

    public abstract String displayName();

    public String generateToolRequestResult() {
        return String.format("\n\n选择工具%s\n\n", displayName());
    }

    public abstract String generateToolExecutedResult(JSONObject jsonObject);
}
