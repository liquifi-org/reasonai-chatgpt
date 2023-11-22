package ai.reason.chatgpt.common.prompt;

import java.util.List;

public class PromptTemplate {

    private static final String template = """
            Context information is below.
            ---------------------
            %s
            ---------------------
            Given the context information and all possible prior knowledge, answer the question: %s
            The answer should be comprehensive without any separation between context data and prior knowledge, and detailed as much as possible.
            """;

    public static String formatWithContext(List<String> context, String question) {
        String merged = String.join("\n", context);
        return String.format(template, merged, question);
    }
}
