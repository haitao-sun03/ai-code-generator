package com.haitao.generator.model.request.app;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * 创建应用请求
 *
 * @author haitao
 */
@Data
public class AppAddRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 初始提示词
     */
    @NotBlank(message = "初始提示词不能为空")
    private String initPrompt;

}