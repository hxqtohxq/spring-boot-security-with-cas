package vn.com.vndirect.directboardservice.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import vn.com.vndirect.onlineuserservice.Application;
import vn.com.vndirect.onlineuserservice.model.WatchedListEntity;
import vn.com.vndirect.onlineuserservice.repository.WatchedSymbolsRepository;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class WatchedListRepositoryTest {
	
	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;
	
	@Autowired
	private WatchedSymbolsRepository repository;
	
	public void setUp() throws Exception {
		elasticsearchTemplate.deleteIndex(WatchedListEntity.class);
		elasticsearchTemplate.createIndex(WatchedListEntity.class);
		elasticsearchTemplate.putMapping(WatchedListEntity.class);
		elasticsearchTemplate.refresh(WatchedListEntity.class, true);
	}
	
	@Test
	public void testAddNew() {
		String customerId = UUID.randomUUID().toString();
		WatchedListEntity sampleEntity = new WatchedListEntity();
		sampleEntity.setCustomerId(customerId);
		
		sampleEntity.setSymbols(_generateList("vnd", "aaa", "abc"));
		
		repository.save(sampleEntity);
		
		WatchedListEntity result = repository.findOne(customerId);
		
		assertEquals(result.getCustomerId(), customerId);
		assertTrue(result.getSymbols().size() == 3);
		
		assertEquals("vnd", result.getSymbols().toArray()[0]);
	}
	
	@Test
	public void testUpdate() {
		String customerId = UUID.randomUUID().toString();
		// save a new entity
		WatchedListEntity sampleEntity = new WatchedListEntity();
		sampleEntity.setCustomerId(customerId);
		sampleEntity.setSymbols(_generateList("vnd", "aaa", "aaa", "abc"));
		repository.save(sampleEntity);
		
		
		// update
		WatchedListEntity existed = repository.findOne(customerId);
		List<String> symbols = _generateList("vnd"); 
		existed.setSymbols(symbols);
		repository.save(existed);
		
		WatchedListEntity afterUpdate = repository.findOne(customerId);
		
		assertEquals(afterUpdate.getSymbols(), symbols);
		
	}
	

	private List<String> _generateList(String... arr) {
		List<String> res = new ArrayList<String>();
		for(String s : arr) {
			res.add(s);
		}
		
		return res;
	}
	
}
