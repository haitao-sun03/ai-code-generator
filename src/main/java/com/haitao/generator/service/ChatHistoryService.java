package com.haitao.generator.service;

import com.haitao.generator.model.request.history.ChatHistoryQueryRequest;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.haitao.generator.model.entity.ChatHistory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;

import java.time.LocalDateTime;

/**
 * 对话历史记录表 服务层。
 *
 * @author haitao
 */
public interface ChatHistoryService extends IService<ChatHistory> {

    /**
     * 对话记录新增
     * @param appId
     * @param message
     * @param messageType
     * @return
     */
    boolean addChatMessage(long appId,String message,String messageType,long userId);

    /**
     * 删除某appId全部的对话记录
     * @param appId
     * @return
     */
    boolean deleteChatMessageByAppId(long appId);


    /**
     * 构造游标查询请求
     * @param chatHistoryQueryRequest
     * @return
     */
    QueryWrapper getQueryWrapper(ChatHistoryQueryRequest chatHistoryQueryRequest);

    /**
     * 获取对话记录列表，游标查询
     * @param appId
     * @param pageSize
     * @param lastCreateTime
     * @return
     */
    Page<ChatHistory> listAppChatHistoryByPage(Long appId, int pageSize,
                                               LocalDateTime lastCreateTime);

    /**
     * 加载数据库中记录的应用 历史对话记录到redis中，提供给AI对话记忆功能
     * @param appId
     * @param chatMemory
     * @param maxCount
     * @return
     */
    int loadChatHistoryToMemory(Long appId, MessageWindowChatMemory chatMemory, int maxCount);
}
