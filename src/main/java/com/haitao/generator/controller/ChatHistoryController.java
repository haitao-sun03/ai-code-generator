package com.haitao.generator.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.haitao.generator.exception.ErrorCode;
import com.haitao.generator.model.ApiResponse;
import com.haitao.generator.model.request.history.ChatHistoryQueryRequest;
import com.haitao.generator.utils.ThrowUtils;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.haitao.generator.model.entity.ChatHistory;
import com.haitao.generator.service.ChatHistoryService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 对话历史记录表 控制层。
 *
 * @author haitao
 */
@RestController
@RequestMapping("/chatHistory")
public class ChatHistoryController {

    @Autowired
    private ChatHistoryService chatHistoryService;

    /**
     * 分页查询某个应用的对话历史（游标查询）
     *
     * @param appId          应用ID
     * @param pageSize       分页记录数
     * @param lastCreateTime 最后一条记录的创建时间
     * @return 对话历史分页
     */
    @GetMapping("/app/{appId}")
    public ApiResponse<Page<ChatHistory>> listAppChatHistory(@PathVariable Long appId,
                                                             @RequestParam(defaultValue = "10") int pageSize,
                                                             @RequestParam(required = false) LocalDateTime lastCreateTime) {
        Page<ChatHistory> result = chatHistoryService.listAppChatHistoryByPage(appId, pageSize, lastCreateTime);
        return ApiResponse.success(result);
    }


    /**
     * 管理员分页查询所有对话历史
     *
     * @param chatHistoryQueryRequest 查询请求
     * @return 对话历史分页
     */
    @PostMapping("/admin/list/page/vo")
    @SaCheckRole("admin")
    public ApiResponse<Page<ChatHistory>> adminListAllChatHistoryByPage(@RequestBody ChatHistoryQueryRequest chatHistoryQueryRequest) {
        ThrowUtils.throwIf(chatHistoryQueryRequest == null, ErrorCode.PARAMS_ERROR);
        long pageNum = chatHistoryQueryRequest.getPageNum();
        long pageSize = chatHistoryQueryRequest.getPageSize();
        // 查询数据
        QueryWrapper queryWrapper = chatHistoryService.getQueryWrapper(chatHistoryQueryRequest);
        Page<ChatHistory> result = chatHistoryService.page(Page.of(pageNum, pageSize), queryWrapper);
        return ApiResponse.success(result);
    }



}
