package com.haitao.generator.model.request.app;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 更新应用请求
 *
 * @author haitao
 */
@Data
public class AppUpdateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @NotNull(message = "应用id不能为空")
    private Long id;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 应用封面
     */
    private String cover;

    /**
     * 优先级
     */
    private Integer priority;

}