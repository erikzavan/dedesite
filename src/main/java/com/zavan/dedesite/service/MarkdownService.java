package com.zavan.dedesite.service;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import org.springframework.stereotype.Service;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.springframework.stereotype.Service;
    
@Service
public class MarkdownService {
    
    private final Parser parser = Parser.builder().build();
    private final HtmlRenderer renderer = HtmlRenderer.builder().build();

    private static final PolicyFactory POLICY = new HtmlPolicyBuilder()
        .allowElements("a","p","h1","h2","h3","h4","h5","h6",
                        "ul","ol","li","strong","em","blockquote",
                        "pre","code","img","hr","br","span","div",
                        "table","thead","tbody","tr","th","td")
        .allowUrlProtocols("http","https","data")
        .allowAttributes("href").onElements("a")
        .allowAttributes("src","alt","title").onElements("img")
        .allowAttributes("class","id").onElements("span","div","pre","code","table","thead","tbody","tr","th","td")
        .requireRelNofollowOnLinks()
        .toFactory();
    
    public String toSafeHtml(String markdown) {
        String md = markdown == null ? "" : markdown;
        Node doc = parser.parse(md);
        String rawHtml = renderer.render(doc);
        return POLICY.sanitize(rawHtml);
    }
}   