package per.lijun.hannah.entity;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Book {

    private Integer bookId;

    private String name;

    private String sysnopsis;

    private Date gmtCreate;

    private Date gmtModified;

    private String author;

    private String category;

}
