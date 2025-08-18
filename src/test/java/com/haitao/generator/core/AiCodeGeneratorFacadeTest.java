package com.haitao.generator.core;

import com.haitao.generator.enums.CodeGenTypeEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AiCodeGeneratorFacadeTest {

    @Autowired
    private AiCodeGeneratorFacade aiCodeGeneratorFacade;

    @Test
    void generateAndSaveCode() {
        File file = aiCodeGeneratorFacade.generateAndSaveCode("帮我生成一个登录页面，长度不超过20行", CodeGenTypeEnum.HTML, 10L);
        Assertions.assertNotNull(file);
    }

    @Test
    void generateAndSaveCodeStream() {
        Flux<String> stringFlux = aiCodeGeneratorFacade.generateAndSaveCodeStream("帮我生成一个登录页面，长度不超过20行", CodeGenTypeEnum.MULTI_FILE, 10L);
        List<String> block = stringFlux.collectList().block();
        Assertions.assertNotNull(block);
        String allContent = String.join("", block);
        Assertions.assertNotNull(allContent);
    }
}