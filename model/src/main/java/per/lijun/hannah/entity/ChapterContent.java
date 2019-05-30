package per.lijun.hannah.entity;

import lombok.Data;

@Data
public class ChapterContent {

    private int pageId;

    private int next;

    private String content;

    private int chapterId;

    private int pre;
}
