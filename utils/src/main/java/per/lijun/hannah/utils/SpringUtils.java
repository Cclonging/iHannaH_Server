package per.lijun.hannah.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 手动获取Spring的bean管理对象, 专门解决在Handler这样的场景中无法使用Spring注解的情况
 */
@Component
public class SpringUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (Objects.isNull(SpringUtils.applicationContext)){
            SpringUtils.applicationContext = applicationContext;
        }
    }

    // 获取applicationContext
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    //通过bean 的Name来获取其所对应的bean对象
    public static Object getBean(String beanName){
        return getApplicationContext().getBean(beanName);
    }

    //通过Class获取bean
    public static<T> T getBean(Class<T> aClass){
        return getApplicationContext().getBean(aClass);
    }

    //通过class以及beanname来获取bean
    public static <T> T getBean(String beanName, Class<T> aClass){
        return getApplicationContext().getBean(beanName, aClass);
    }


}
