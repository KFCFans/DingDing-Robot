package com.tmall.marketing.dingdingrobot.dai.model;

import lombok.Data;

/**
 * 古诗词
 * {"content":"燕子来时新社，梨花落后清明。","origin":"破阵子·春景","author":"晏殊","category":"古诗文-节日-清明节"}
 *
 * @author tjq
 * @since 2020/7/20
 */
@Data
public class Poetry {
    private String content;
    private String origin;
    private String author;
    private String category;

    public static Poetry defaultPoetry() {
        Poetry p = new Poetry();
        p.content = "早点入职，早点离职。";
        p.origin = "《劝退》";
        p.author = "CPP";
        p.category = "劝退";
        return p;
    }

    public String mini() {
        return String.format("%s  —— %s《%s》", content, author, origin);
    }
}
