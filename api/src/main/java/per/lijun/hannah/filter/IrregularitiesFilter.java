package per.lijun.hannah.filter;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.tag.Nature;
import com.hankcs.hanlp.seg.common.Term;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import per.lijun.hannah.entity.IllegalRemarks;
import per.lijun.hannah.idworker.Sid;

import java.beans.Transient;
import java.util.*;


/**
 * 违规文字过滤器
 */
@Component
@Slf4j
public class IrregularitiesFilter {
    private static String BAN_WORDS = null;

    private static List<IllegalRemarks> illegalRemarks = null;

    public static MongoCollection<Document> illegalUrlArgs = null;


    @Autowired
    private MongoTemplate template;

    @Autowired
    private Sid sid;
    static {
        //TODO 从数据里面拿出数据
        BAN_WORDS = "毛泽东胡锦涛江泽民邓小平习近平fuck南京工程学院";

        illegalRemarks= new ArrayList<>(2);

    }


    /**
     * 分词检测
     *
     * @param words
     */
    public String fenci(String words, String sendId, String receiveId) {
        List<Term> terms = HanLP.segment(words);
        //拿出名词
        if (!terms.isEmpty()) {
            ListIterator<Term> iterable = terms.listIterator();
            StringBuilder sb = new StringBuilder();
            while (iterable.hasNext()) {
                Term item = iterable.next();
                //System.out.println(item.nature + item.word);
                if (Objects.equals(item.nature, Nature.n) ||
                        Objects.equals(item.nature, Nature.nr) ||
                        Objects.equals(item.nature, Nature.nx)) {
                    //调用违规词库检测代码， 用**替换
                    item.word = generatorStars(words, item.word, sendId, receiveId);
                }
                sb.append(item.word);
            }
//            if (!illegalRemarks.isEmpty()){
//                template.save(illegalRemarks);
//                illegalRemarks.clear();
//            }
            return sb.toString();
        }
        return words;
    }

    /**
     * 生成**
     *
     * @param n
     * @return
     */
    @Transient
    public String generatorStars(String words, String n, String sendId, String receiveId) {
        if (isBan(n)) {
            IllegalRemarks illegalRemarks = new IllegalRemarks(sid.nextShort(),"违规言论",sendId,receiveId,words,new Date().toString());
            template.save(illegalRemarks);
            int len = n.matches("[a-zA-Z]+") ? 1 : n.length();
            StringBuilder sb = new StringBuilder(len);
            for (int i = 0; i < len; i++) {
                sb.append("*");
            }
            return sb.toString();
        }
        return n;
    }

    /**
     *
     */
    public boolean isBan(String word) {
        //从缓存中拿出数据
        if (BAN_WORDS != null) {
            if (BAN_WORDS.indexOf(word) != -1)
                return true;
        }
        //TODO 从数据库里面提取数据
        return false;
    }
}
