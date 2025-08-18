package com.haitao.generator.mapper;

import com.mybatisflex.core.BaseMapper;
import com.haitao.generator.model.entity.App;
import org.apache.ibatis.annotations.Mapper;

/**
 * 应用表 映射层。
 *
 * @author haitao
 */
@Mapper
public interface AppMapper extends BaseMapper<App> {

}