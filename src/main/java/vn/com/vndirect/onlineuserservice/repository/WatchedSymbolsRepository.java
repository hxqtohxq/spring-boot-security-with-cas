package vn.com.vndirect.onlineuserservice.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import vn.com.vndirect.onlineuserservice.model.WatchedListEntity;

public interface WatchedSymbolsRepository extends ElasticsearchRepository<WatchedListEntity, String> {

}
