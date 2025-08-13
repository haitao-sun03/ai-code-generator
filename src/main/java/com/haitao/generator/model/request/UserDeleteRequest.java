package com.haitao.generator.model.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

@Data
public class UserDeleteRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @NotBlank(message = "id不能为空")
    @Min(value = 1,message = "用户合法id必须大于0")
    private Long id;

}
