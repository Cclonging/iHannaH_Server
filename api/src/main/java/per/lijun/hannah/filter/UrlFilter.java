package per.lijun.hannah.filter;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.bson.Document;
import org.springframework.stereotype.Component;
import per.lijun.illegalRemarks.filter.IrregularitiesFilter;
import java.util.Date;
import java.util.Objects;

/**
 * 记录违规的url参数， 并设置无效
 */
@Component
@Aspect
@Slf4j
public class UrlFilter {

    @Pointcut("execution(public * per.lijun.hannah.controller.*Controller.*(..))")
    public void banSqlInsert(){}

    @Before("banSqlInsert()")
    public void beforeRequestAjax(JoinPoint joinPoint){
        for (Object obj : joinPoint.getArgs()){
            if (!Objects.isNull(obj)){
                if (obj.toString().matches("insert") || obj.toString().matches("update")){
                    Document document = new Document().append("title", "非法url")
                            .append("content", obj.toString())
                                   .append("date", new Date());
                    IrregularitiesFilter.illegalUrlArgs.insertOne(document);
                    log.info("非法url参数： " + obj);
                }
            }

        }
    }


}
