package com.haitao.generator.model.request.app;

import com.haitao.generator.model.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 应用查询请求
 *
 * @author haitao
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AppQueryRequest extends PageRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 应用生成类型
     */
    private String codeGenType;

}