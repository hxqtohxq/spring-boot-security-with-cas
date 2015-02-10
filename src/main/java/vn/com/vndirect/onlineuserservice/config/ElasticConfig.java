package vn.com.vndirect.onlineuserservice.config;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

@Configuration
public class ElasticConfig {

    @Value("${elastic.address}")
    private String address;

    @Value("${elastic.port}")
    private int port;
    
    @Value("${elastic.clusterName}")
    private String clusterName;
    
    @Bean
    public Client elasticSearchClient() {
    	Settings settings = ImmutableSettings.settingsBuilder().put("cluster.name", clusterName).build();
        TransportClient transportClient = new TransportClient(settings);
        transportClient.addTransportAddresses(new InetSocketTransportAddress(address, port));
        return transportClient;
    }
    
    @Bean
    public ElasticsearchTemplate elasticsearchTemplate() {
    	return new ElasticsearchTemplate(elasticSearchClient());
    }
}
