package per.lijun.hannah.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import per.lijun.hannah.entity.Book;
import per.lijun.hannah.entity.Category;
import per.lijun.hannah.entity.Chapter;
import per.lijun.hannah.entity.ChapterContent;
import per.lijun.hannah.mapper.BookMapper;
import per.lijun.hannah.service.BookService;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookMapper bookMapper;

    @Override
    public Book getBookById(Integer id) {
        return bookMapper.selectBookById(id);
    }

    @Override
    public List<Book> getBooksByCa(int ca) {
        return bookMapper.selectBooksByCa(ca);
    }

    @Override
    public List<Category> getAllBooksCa(int start, int end) {
        return bookMapper.selectAllBooksCa(start, end);
    }

    @Override
    public List<Chapter> getChaptersByBoId(int boId) {
        return bookMapper.selectChaptersByid(boId);
    }

    @Override
    public ChapterContent getContentByPage(int page) {
        return bookMapper.selectContentByPage(page);
    }

    @Override
    public List<Book> getAllBooks(int start, int end) {
        return bookMapper.selectAllBooks(start, end);
    }

    @Override
    public List<Book> getBooksByName(String name) {
        return bookMapper.selectBookByName(name);
    }

    @Override
    public int getPageIndex(int bookId) {
        return bookMapper.selectPageIndex(bookId);
    }


}
