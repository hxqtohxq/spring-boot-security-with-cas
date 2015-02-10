package vn.com.vndirect.onlineuserservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import vn.com.vndirect.onlineuserservice.handler.Handler;

@EnableAutoConfiguration
@ComponentScan
@EnableElasticsearchRepositories("vn.com.vndirect.onlineuserservice.repository")
@PropertySource("classpath:/configure.properties")
public class Application implements CommandLineRunner {
	
	@Autowired
	@Qualifier("PrepareElasticDocumentHandler")
	private Handler prepareElasticDocumentHandler;
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... arg0) throws Exception {
		prepareElasticDocumentHandler.handle(null);
	}
}
