package vn.com.vndirect.onlineuserservice.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class WatchedSymbol implements Serializable {

	private String customerId;
	private String symbol;
	
	public WatchedSymbol() {
		// explicit default constructor to enable bean parameter of restful request
	}

	public String getCustomerId() {
		return customerId;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol.toUpperCase(); // auto upper-case
	}

	@Override
	public String toString() {
		return "WatchedSymbol [customerId=" + customerId + ", symbol=" + symbol + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((customerId == null) ? 0 : customerId.hashCode());
		result = prime * result + ((symbol == null) ? 0 : symbol.hashCode());
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
		WatchedSymbol other = (WatchedSymbol) obj;
		if (customerId == null) {
			if (other.customerId != null)
				return false;
		} else if (!customerId.equals(other.customerId))
			return false;
		if (symbol == null) {
			if (other.symbol != null)
				return false;
		} else if (!symbol.equals(other.symbol))
			return false;
		return true;
	}
}
