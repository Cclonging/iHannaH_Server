package per.lijun.hannah.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import per.lijun.hannah.entity.Book;
import per.lijun.hannah.entity.Category;

import java.util.List;

@Data
@AllArgsConstructor
public class BookIndex {

    private List<Category> ca;

    private List<Book> intro;

    private List<Book> last;
}
