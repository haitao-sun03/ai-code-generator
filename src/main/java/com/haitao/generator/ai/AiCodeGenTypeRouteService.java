package com.haitao.generator.ai;

import com.haitao.generator.enums.CodeGenTypeEnum;
import dev.langchain4j.service.SystemMessage;

public interface AiCodeGenTypeRouteService {

    @SystemMessage(fromResource="prompt/router-prompt.txt")
    CodeGenTypeEnum routeToCodeGenType(String userMessage);
}
