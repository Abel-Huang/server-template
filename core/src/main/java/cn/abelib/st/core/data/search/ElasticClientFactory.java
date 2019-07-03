package cn.abelib.st.core.data.search;

import cn.abelib.st.core.config.ElasticSearchConfig;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.InetAddress;
import java.net.UnknownHostException;


/**
 *
 * @author abel
 * @date 2018/4/12
 */
@Slf4j
public class ElasticClientFactory {
    @Autowired
    private ElasticSearchConfig elasticConfig;

    public TransportClient esClient() throws UnknownHostException    {
        Settings settings = Settings.builder()
                //设置集群名称
                .put("cluster.name", elasticConfig.getClusterName())
                //开启嗅探集群的状态
                .put("client.transport.sniff", true)
                .build();
        InetSocketTransportAddress master = new InetSocketTransportAddress(
                    InetAddress.getByName(elasticConfig.getHost()), elasticConfig.getPort()
            );
        TransportClient transportClient = new PreBuiltTransportClient(settings)
                    .addTransportAddress(master);
        return transportClient;
    }
}
