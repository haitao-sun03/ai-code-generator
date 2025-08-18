package com.haitao.generator.model.request.app;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 删除应用请求
 *
 * @author haitao
 */
@Data
public class AppDeleteRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @NotNull(message = "应用id不能为空")
    private Long id;

}