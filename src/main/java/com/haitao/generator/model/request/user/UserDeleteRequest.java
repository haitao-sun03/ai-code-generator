package com.haitao.generator.model.request.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserDeleteRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @NotBlank(message = "id不能为空")
    private String id;

}
