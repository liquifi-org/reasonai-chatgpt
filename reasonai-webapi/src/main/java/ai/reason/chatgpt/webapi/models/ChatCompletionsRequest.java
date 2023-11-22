package ai.reason.chatgpt.webapi.models;

import com.azure.ai.openai.models.ChatMessage;
import lombok.Data;

import java.util.List;

@Data
public class ChatCompletionsRequest {
    private List<ChatMessage> messages;
}
