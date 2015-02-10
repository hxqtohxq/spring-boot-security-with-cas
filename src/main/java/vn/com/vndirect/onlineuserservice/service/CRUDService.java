package vn.com.vndirect.onlineuserservice.service;

public interface CRUDService<I, O> {
	O get(I request);

	void add(I request);

	void remove(I request);
}
