package com.haitao.generator.ai.model;

import dev.langchain4j.model.output.structured.Description;
import lombok.Data;


@Data
@Description("生成html代码结果文件")
public class HtmlCodeResult {

    @Description("html代码")
    private String htmlCode;

    @Description("生成的代码描述")
    private String description;
}
