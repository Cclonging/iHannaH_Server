package per.lijun.hannah.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import per.lijun.hannah.entity.Book;
import per.lijun.hannah.entity.Category;
import per.lijun.hannah.entity.Chapter;
import per.lijun.hannah.entity.ChapterContent;

import java.util.List;

public interface BookMapper {

    @Select("select bookId, name, sysnopsis, gmtCreate, author, category from view_book where bookid = #{id}")
    Book selectBookById(int id);

    @Select("select book_id as bookId, book_name as name from book where book_ca_id = #{ca}")
    List<Book> selectBooksByCa(int ca);

    @Select("select ca_id as caId, ca_name as category from book_category limit #{param1}, #{param2}")
    List<Category> selectAllBooksCa(int start, int end);

    @Select("select chapterId, cur_chapter as chapter, page_index as pindex from book_chapter where book_id = #{boId}")
    List<Chapter> selectChaptersByid(int boId);

    @Select("select page_id as pageId, page_content as content, next_page as next, pre_page as pre, chapter_id as chapterId from chapter_content where page_id = #{page}")
    ChapterContent selectContentByPage(int page);

    @Select("select book_id as bookId, book_name as name from book where 1=1 limit #{param1}, #{param2}")
    List<Book> selectAllBooks(int start, int end);

    @Select("select bookId, name, sysnopsis, gmtCreate, author, category from view_book where name like '%${name}%'")
    List<Book> selectBookByName(@Param("name") String name);

    @Select("select page_index as i from book_chapter where book_id = #{bookId} limit 0,1;")
    int selectPageIndex(int bookId);
}
