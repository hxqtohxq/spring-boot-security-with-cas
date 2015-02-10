package vn.com.vndirect.onlineuserservice.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.com.vndirect.onlineuserservice.model.WatchedListEntity;
import vn.com.vndirect.onlineuserservice.model.WatchedSymbol;
import vn.com.vndirect.onlineuserservice.repository.WatchedSymbolsRepository;

@Service
public class WatchedListService implements CRUDService<WatchedSymbol, WatchedListEntity>{
	
	private WatchedSymbolsRepository repository;
	
	@Autowired
	public WatchedListService(WatchedSymbolsRepository repository) {
		this.repository = repository;
	}

	@Override
	public WatchedListEntity get(WatchedSymbol request) {
		return repository.findOne(request.getCustomerId());
	}

	@Override
	public void add(WatchedSymbol request) {
		WatchedListEntity entity = createAddingEntity(request);
		repository.save(entity);
	}

	@Override
	public void remove(WatchedSymbol request) {
		WatchedListEntity entity = createRemoveEntity(request);
		repository.save(entity);
	}
	
	private WatchedListEntity createRemoveEntity(WatchedSymbol request) {
		WatchedListEntity existed = repository.findOne(request.getCustomerId());
		
		if(existed == null) {
			throw new RuntimeException("Cannot remove symbol from an empty entity ..." ); 
		} else {
			return updateExistedEntityForRemoving(existed, request);
		}
	}
	
	private WatchedListEntity createAddingEntity(WatchedSymbol request) {
		WatchedListEntity existed = repository.findOne(request.getCustomerId());
		
		if(existed == null) {
			return newEntity(request);
		} else {
			return updateExistedEntityForAdding(existed, request);
		}
	}
	
	private WatchedListEntity updateExistedEntityForAdding(WatchedListEntity existed, WatchedSymbol request){
		List<String> symbols = existed.getSymbols();
		if(symbols == null) {
			symbols = new ArrayList<String>();
		}
		if (!symbols.contains(request.getSymbol())){
			symbols.add(request.getSymbol());
		}
		existed.setSymbols(symbols);
		
		return existed;
	}
	
	private WatchedListEntity updateExistedEntityForRemoving(WatchedListEntity existed, WatchedSymbol request){
		List<String> symbols = existed.getSymbols();
		if(symbols == null || symbols.isEmpty()) {
			return existed; // do nothing
		}
		
		symbols.remove(request.getSymbol());
		existed.setSymbols(symbols);
		
		return existed;
	}
	
	private WatchedListEntity newEntity(WatchedSymbol request) {
		List<String> symbols = new ArrayList<String>();
		symbols.add(request.getSymbol());

		WatchedListEntity entity = new WatchedListEntity();
		entity.setCustomerId(request.getCustomerId());
		entity.setSymbols(symbols);
		
		return entity;
	}
}	
