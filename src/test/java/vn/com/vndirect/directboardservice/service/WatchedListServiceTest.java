package vn.com.vndirect.directboardservice.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import vn.com.vndirect.onlineuserservice.model.WatchedListEntity;
import vn.com.vndirect.onlineuserservice.model.WatchedSymbol;
import vn.com.vndirect.onlineuserservice.repository.WatchedSymbolsRepository;
import vn.com.vndirect.onlineuserservice.service.WatchedListService;

public class WatchedListServiceTest {
	
	private WatchedSymbolsRepository repository;
	
	private WatchedListService service;
	
	private ArgumentCaptor<WatchedListEntity> captor;

	@Before
	public void setUp() throws Exception {
		repository = mock(WatchedSymbolsRepository.class);
		service = new WatchedListService(repository);
		
		captor = ArgumentCaptor.forClass(WatchedListEntity.class);
	}
	
	@Test
	public void removeSymbol() {
		String symbol = "symbol";
		String customerId = "customerId";
		
		when(repository.findOne(customerId)).thenReturn(_createEntity(customerId, symbol));

		service.remove(_createWatchedSymbol(customerId, symbol));
		
		verify(repository).save(captor.capture());
		
		WatchedListEntity captured = captor.getValue();
		
		assertNotNull(captured);
		assertEquals(customerId, captured.getCustomerId());
		assertEquals(0, captured.getSymbols().size());
	}
	
	@Test
	public void removeNotExistedSymbol() {
		String symbol = "symbol";
		String customerId = "customerId";
		
		when(repository.findOne(customerId)).thenReturn(_createEntity(customerId, symbol));

		service.remove(_createWatchedSymbol(customerId, "new symbol"));
		
		verify(repository).save(captor.capture());
		
		WatchedListEntity captured = captor.getValue();
		
		assertNotNull(captured);
		assertEquals(customerId, captured.getCustomerId());
		assertEquals(1, captured.getSymbols().size());
		assertTrue(captured.getSymbols().contains(symbol.toUpperCase()));
	}
	
	@Test
	public void removeSymbolWhenTheExistSymbolsListIsNull() {
		String symbol = "symbol";
		String customerId = "customerId";
		
		WatchedListEntity existed = _createEntity(customerId);
		existed.setSymbols(null);
		when(repository.findOne(customerId)).thenReturn(existed);

		service.remove(_createWatchedSymbol(customerId, symbol));
		
		verify(repository).save(captor.capture());
		
		WatchedListEntity captured = captor.getValue();
		
		assertNotNull(captured);
		assertEquals(customerId, captured.getCustomerId());
		assertTrue(captured.getSymbols() == null);
	}
	
	@Test
	public void removeSymbolWhenTheExistSymbolsListIsEmpty() {
		String symbol = "symbol";
		String customerId = "customerId";
		
		when(repository.findOne(customerId)).thenReturn(_createEntity(customerId));

		service.remove(_createWatchedSymbol(customerId, symbol));
		
		verify(repository).save(captor.capture());
		
		WatchedListEntity captured = captor.getValue();
		
		assertNotNull(captured);
		assertEquals(customerId, captured.getCustomerId());
		assertEquals(0, captured.getSymbols().size());
	}
	
	@Test
	public void removeSymbolWhenTheEntityNotExist() {
		String symbol = "symbol";
		String customerId = "customerId";
		
		when(repository.findOne(customerId)).thenReturn(null);

		try {
			service.remove(_createWatchedSymbol(customerId, symbol));
			fail("should throw exception");
		} catch (Exception e) {
			assertNotNull(e);
		}
	}
	
	@Test
	public void addSymbolToExistEntityWhichTheSymbolIsContained() {
		String symbol = "symbol";
		String customerId = "customerId";
		
		when(repository.findOne(customerId)).thenReturn(_createEntity(customerId, "symbol"));

		service.add(_createWatchedSymbol(customerId, symbol));
		
		verify(repository).save(captor.capture());
		
		WatchedListEntity captured = captor.getValue();
		
		assertNotNull(captured);
		assertEquals(customerId, captured.getCustomerId());
		assertEquals(1, captured.getSymbols().size());
		assertTrue(captured.getSymbols().contains(symbol.toUpperCase()));
	}
	
	@Test
	public void addSymbolToExistEntityWhichTheSymbolIsNotContainsYet() {
		String symbol = "symbol";
		String customerId = "customerId";
		
		when(repository.findOne(customerId)).thenReturn(_createEntity(customerId, "existedSymbol"));

		service.add(_createWatchedSymbol(customerId, symbol));
		
		verify(repository).save(captor.capture());
		
		WatchedListEntity captured = captor.getValue();
		
		assertNotNull(captured);
		assertEquals(customerId, captured.getCustomerId());
		assertEquals(2, captured.getSymbols().size());
		assertTrue(captured.getSymbols().contains(symbol.toUpperCase()));
	}

	@Test
	public void addNewEntity() {
		String symbol = "symbol";
		String customerId = "customerId";

		when(repository.findOne(customerId)).thenReturn(null);
		
		service.add(_createWatchedSymbol(customerId, symbol));
		
		verify(repository).save(captor.capture());
		
		WatchedListEntity captured = captor.getValue();
		
		assertNotNull(captured);
		assertEquals(customerId, captured.getCustomerId());
		assertEquals(1, captured.getSymbols().size());
		assertTrue(captured.getSymbols().contains(symbol.toUpperCase()));
	}
	
	
	private WatchedSymbol _createWatchedSymbol(String customerId, String symbol) {
		WatchedSymbol watchedSymbol = new WatchedSymbol();
		watchedSymbol.setSymbol(symbol);
		watchedSymbol.setCustomerId(customerId);

		return watchedSymbol;
	}
	

	private WatchedListEntity _createEntity(String customerId, String... symbols) {
		List<String> list = new ArrayList<String>();
		for(String s : symbols){
			list.add(s.toUpperCase());
		}

		WatchedListEntity existed = new WatchedListEntity();
		existed.setCustomerId(customerId);
		existed.setSymbols(list);
		
		return existed;
	}
}
