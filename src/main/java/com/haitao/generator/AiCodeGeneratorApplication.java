package com.haitao.generator;

import dev.langchain4j.community.store.embedding.redis.spring.RedisEmbeddingStoreAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {RedisEmbeddingStoreAutoConfiguration.class})
@MapperScan(basePackages = "com.haitao.generator.mapper")
public class AiCodeGeneratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiCodeGeneratorApplication.class, args);
    }

}
