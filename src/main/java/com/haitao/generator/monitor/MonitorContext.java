package com.haitao.generator.monitor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 封装userId,appId，供大模型调用时传递
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonitorContext implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userId;

    private String appId;

}
