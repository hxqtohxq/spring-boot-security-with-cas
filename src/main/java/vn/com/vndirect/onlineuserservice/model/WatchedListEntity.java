package vn.com.vndirect.onlineuserservice.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * Store watched list direct-board of each customer
 */
@Document(indexName = "db-watchedlist", type = "watchedlist", indexStoreType = "memory", shards = 5, replicas = 0, refreshInterval = "-1")
public class WatchedListEntity {

	@Id
	private String customerId;

	private List<String> symbols;

	public String getCustomerId() {
		return customerId;
	}

	public List<String> getSymbols() {
		return symbols;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public void setSymbols(List<String> symbols) {
		this.symbols = symbols;
	}

	@Override
	public String toString() {
		return "WatchedListEntity [customerId=" + customerId + ", symbols=" + symbols + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((customerId == null) ? 0 : customerId.hashCode());
		result = prime * result + ((symbols == null) ? 0 : symbols.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WatchedListEntity other = (WatchedListEntity) obj;
		if (customerId == null) {
			if (other.customerId != null)
				return false;
		} else if (!customerId.equals(other.customerId))
			return false;
		if (symbols == null) {
			if (other.symbols != null)
				return false;
		} else if (!symbols.equals(other.symbols))
			return false;
		return true;
	}
}
