package per.lijun.hannah.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import per.lijun.hannah.entity.Chapter;
import per.lijun.hannah.entity.ChapterContent;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ContentVo {
    private ChapterContent chapterContent;

    private Integer bookId;
}
