package per.lijun.hannah.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import per.lijun.hannah.entity.Book;
import per.lijun.hannah.entity.Chapter;

import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class BookInfo {

    private List<Chapter> chapters;

    private Book book;
}
