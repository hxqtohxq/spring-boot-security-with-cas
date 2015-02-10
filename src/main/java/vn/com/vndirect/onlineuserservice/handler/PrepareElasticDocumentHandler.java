package vn.com.vndirect.onlineuserservice.handler;

import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Component;

@Component("PrepareElasticDocumentHandler")
public class PrepareElasticDocumentHandler implements Handler {

	private ElasticsearchTemplate template;

	@Autowired
	public PrepareElasticDocumentHandler(ElasticsearchTemplate template) {
		this.template = template;
	}

	@Override
	public void handle(Object message) {
		Reflections reflections = new Reflections(new ConfigurationBuilder().setUrls(
				ClasspathHelper.forPackage("vn.com.vndirect.onlineuserservice.model")).setScanners(new TypeAnnotationsScanner()));

		Set<Class<?>> elasticDocuments = reflections.getTypesAnnotatedWith(Document.class);

		for (Class<?> document : elasticDocuments) {
			if (!template.indexExists(document)) {
				template.createIndex(document);
			}
		}
	}

}
