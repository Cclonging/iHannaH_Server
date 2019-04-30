package per.lijun.hannah.netty.boot;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import per.lijun.hannah.netty.WsServer;

@Component
public class NettyBoot implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null){
            try {
                WsServer.getInstance().start();
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
}
