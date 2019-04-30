import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "per.lijun.hannah.mapper")
@ComponentScan(basePackages = {"per.lijun.hannah"})
public class HannahApplication {

    public static void main(String[] args) {
        SpringApplication.run(HannahApplication.class, args);
    }
}
