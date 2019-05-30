package per.lijun.hannah.service;

import per.lijun.hannah.entity.Book;
import per.lijun.hannah.entity.Category;
import per.lijun.hannah.entity.Chapter;
import per.lijun.hannah.entity.ChapterContent;

import java.util.List;

public interface BookService {

    Book getBookById(Integer id);

    List<Book> getBooksByCa(int ca);

    List<Category> getAllBooksCa(int start, int end);

    List<Chapter> getChaptersByBoId(int BoId);

    ChapterContent getContentByPage(int page);

    List<Book> getAllBooks(int start, int end);

    List<Book> getBooksByName(String name);

    int getPageIndex(int bookId);
}
