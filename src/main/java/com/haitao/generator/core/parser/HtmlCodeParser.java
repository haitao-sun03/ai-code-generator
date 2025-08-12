package com.haitao.generator.core.parser;

import com.haitao.generator.ai.model.HtmlCodeResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlCodeParser implements CodeParser<HtmlCodeResult> {
    private static final Pattern HTML_CODE_PATTERN = Pattern.compile("```html\\s*\\n([\\s\\S]*?)```", Pattern.CASE_INSENSITIVE);

    @Override
    public HtmlCodeResult parseCode(String codeContent) {
        HtmlCodeResult res = new HtmlCodeResult();
        String htmlCode = extractHtmlCode(codeContent);
        if(htmlCode != null && !htmlCode.trim().isEmpty()) {
            res.setHtmlCode(htmlCode);
        }else {
            res.setHtmlCode(codeContent.trim());
        }
        return res;
    }

    /**
     * 提取HTML代码内容
     *
     * @param content 原始内容
     * @return HTML代码
     */
    private static String extractHtmlCode(String content) {
        Matcher matcher = HTML_CODE_PATTERN.matcher(content);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
