package per.lijun.hannah.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import per.lijun.hannah.entity.Book;
import per.lijun.hannah.entity.Category;
import per.lijun.hannah.entity.Chapter;
import per.lijun.hannah.entity.ChapterContent;
import per.lijun.hannah.entity.vo.BookIndex;
import per.lijun.hannah.entity.vo.BookInfo;
import per.lijun.hannah.entity.vo.ContentVo;
import per.lijun.hannah.service.BookService;
import per.lijun.hannah.utils.JSONResult;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private RedisTemplate redisTemplate;


    @GetMapping("/getBookById/{bookId}/info")
    private JSONResult getBookById(@PathVariable("bookId") String id) {
        if (Objects.isNull(id)) {
            return JSONResult.errorMsg("bookid is null");
        }
        Book res = bookService.getBookById(Integer.valueOf(id));
        return JSONResult.ok(res);
    }

    @GetMapping("/getBooksByCa/{category}/info")
    private JSONResult getAllBooks(@PathVariable("category") String category) {
        if (Objects.isNull(category))
            return JSONResult.errorMsg("category is null");
        List<Book> res = bookService.getBooksByCa(Integer.valueOf(category));
        return JSONResult.ok(res);
    }

    @GetMapping("/getAllBooksCa/info")
    private JSONResult getAllBooksCa() {
        return JSONResult.ok(bookService.getAllBooksCa(0, 20));
    }

    @GetMapping("/getChapterByid/{bookId}/info")
    private JSONResult getChaptersByid(@PathVariable("bookId") String bookId) {
        if (Objects.isNull(bookId))
            return JSONResult.errorMsg("book id is null");
        return JSONResult.ok(bookService.getChaptersByBoId(Integer.valueOf(bookId)));
    }

    @GetMapping("/getBook$Chapter/{bookId}/info")
    private JSONResult getBook$Chapter(@PathVariable("bookId") String bookId) {
        if (Objects.isNull(bookId))
            return JSONResult.errorMsg("book id is null");
        List<Chapter> chapters = bookService.getChaptersByBoId(Integer.valueOf(bookId));
        Book book = bookService.getBookById(Integer.valueOf(bookId));
        BookInfo bookInfo = new BookInfo(chapters, book);
        return JSONResult.ok(bookInfo);
    }

    @GetMapping("/getContentByPage/{userId}/{bookId}/{next}/info")
    private JSONResult getContentByPage(@PathVariable("userId") String userId,
                                        @PathVariable("bookId") String bookId,
                                        @PathVariable("next") String next) {
        if (Objects.isNull(userId) || Objects.isNull(bookId) || Objects.isNull(next)) {
            return JSONResult.errorMsg("params is null");
        }

        String key = userId + "_" + bookId;
        String tmp = (String) redisTemplate.opsForValue().get(key);
        int page = bookService.getPageIndex(Integer.valueOf(bookId));

        if (next.trim().equals("load")) {
            if (!Objects.isNull(tmp)) {
                page = Integer.valueOf(tmp);
            }
        } else {
            page = Integer.valueOf(next);
        }

        ChapterContent chapterContent = bookService.getContentByPage(page);
        redisTemplate.opsForValue().set(key, String.valueOf(page));
        ContentVo contentVo = new ContentVo(chapterContent, Integer.valueOf(bookId));
        return JSONResult.ok(contentVo);
    }


    @GetMapping("/getIndex/info")
    private JSONResult getIndex() {
        List<Book> intro = bookService.getAllBooks(0, 3);
        List<Category> ca = bookService.getAllBooksCa(0, 3);
        List<Book> last = intro.subList(0, 2);
        BookIndex bookIndex = new BookIndex(ca, intro, last);
        return JSONResult.ok(bookIndex);
    }

    @GetMapping("/getBookByName/{bookName}/info")
    private JSONResult getBooksByName(@PathVariable("bookName") String name) {
        if (Objects.isNull(name) || name.trim().equals("")) {
            return JSONResult.errorMsg("book name is null");
        }
        List<Book> list = bookService.getBooksByName(name);
        if (list.size() == 0) {
            return JSONResult.errorMsg("系统找不到这样的书");
        }
        return JSONResult.ok(list);
    }

    @PostMapping("/setUserBookPage/{userId}/{bookId}/{page}")
    private void setUserBookPage(@PathVariable("userId") String userid, @PathVariable("bookId")
            String bookId, @PathVariable("page") String page) {
        if (Objects.isNull(userid) || Objects.isNull(bookId) || Objects.isNull(page)) {
            return;
        }
        String key = userid + "_" + bookId;
        redisTemplate.opsForValue().set(key, page);
    }
}




