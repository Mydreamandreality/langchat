package cn.tycoding.langchat.aigc.endpoint;

import cn.hutool.core.util.StrUtil;
import cn.tycoding.langchat.aigc.entity.AigcMessage;
import cn.tycoding.langchat.aigc.service.AigcMessageService;
import cn.tycoding.langchat.aigc.service.ChatService;
import cn.tycoding.langchat.common.dto.ChatReq;
import cn.tycoding.langchat.common.utils.PromptUtil;
import cn.tycoding.langchat.common.utils.R;
import cn.tycoding.langchat.common.utils.StreamEmitter;
import cn.tycoding.langchat.upms.utils.AuthUtil;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tycoding
 * @since 2024/1/30
 */
@RequestMapping("/aigc/chat/knowledge")
@RestController
@AllArgsConstructor
public class KnowledgeChatEndpoint {

    private final ChatService chatService;
    private final AigcMessageService messageService;

    @PostMapping
    public Object chat(@RequestBody ChatReq req) {
        StreamEmitter emitter = new StreamEmitter();
        req.setEmitter(emitter);
        req.setUserId(String.valueOf(AuthUtil.getUserId()));
        req.setUsername(AuthUtil.getUsername());

        if (StrUtil.isNotBlank(req.getKnowledgeId())) {
            req.setPrompt(PromptUtil.buildDocs(req.getMessage()));
            chatService.docsChat(req);
        } else if (StrUtil.isNotBlank(req.getPromptId())) {
            req.setPrompt(PromptUtil.build(req.getMessage(), req.getPromptText()));
            chatService.chat(req);
        } else {
            req.setPrompt(PromptUtil.build(req.getMessage()));
            chatService.chat(req);
        }
        return emitter.get();
    }

    @GetMapping("/messages/{conversationId}")
    public R messages(@PathVariable String conversationId) {
        List<AigcMessage> list = messageService.getMessages(conversationId, String.valueOf(AuthUtil.getUserId()));
        return R.ok(list);
    }

    @DeleteMapping("/cleanMessage/{conversationId}")
    public R cleanMessage(@PathVariable String conversationId) {
        messageService.clearMessage(conversationId);
        return R.ok();
    }
}
