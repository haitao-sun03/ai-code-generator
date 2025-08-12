package com.haitao.generator.core.parser;

/**
 * parser接口抽象
 */
public interface CodeParser<T> {

    /**
     * 将字符串解析为指定的泛型类型对象
     * @param codeContent
     * @return
     */
    public T parseCode(String codeContent);
}
