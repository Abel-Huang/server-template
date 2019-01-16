package cn.abelib.task;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * @author abel
 * @date 2018/4/5
 *  使用SpringSchedule监控ES集群健康状态
 *  使用HttpClient发送Http请求
 */
@Slf4j
@Component
public class EsMonitor {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JavaMailSender javaMailSender;

    private static final String ES_HEALTH_API = "http://127.0.0.1:9200/_cluster/health";

    //集群健康状况
    private static final String GREEN = "green";
    private static final String YELLOW = "yellow";
    private static final String RED = "red";

    @Scheduled(fixedDelay = 5000)
    public void checkHealth(){
        HttpClient client = HttpClients.createDefault();
        HttpGet get = new HttpGet(ES_HEALTH_API);
        try {
            HttpResponse response = client.execute(get);
            if (response.getStatusLine().getStatusCode() != HttpServletResponse.SC_OK){
                log.error("{}  Can not access ES service normally! ", response.getStatusLine().getStatusCode());
            }else {
                String body = EntityUtils.toString(response.getEntity(), "UTF-8");
                JsonNode result = objectMapper.readTree(body);
                String status = result.get("status").asText();
                switch (status){
                    case GREEN:
                        System.err.println("ES server run normally.");
                        log.debug("ES server run normally.");
                        break;
                    case YELLOW:
                        System.err.println("ES server status YELLOW, please check the server!");
                        log.warn("ES server status YELLOW, please check the server!");
                        break;
                    case RED:
                        System.err.println("ES server status RED, must check the server!");
                        log.error("ES server status RED, must check the server!");
                        break;
                    default:
                        log.error("Unknown status, must check the server!");
                        break;
                }
            }
        }catch (IOException e){
            log.error("IOException {}", e.getMessage());
            e.printStackTrace();
        }
    }



    /**
     *  todo  集成QQ邮箱失败
     * @param messages
     */
    public void sendMail(String messages){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("huangjin@abelib.cn");
        mailMessage.setFrom("120972438@qq.com");
        mailMessage.setSubject("【警告】ES监控服务");
        mailMessage.setText(messages);
        javaMailSender.send(mailMessage);
    }
}
