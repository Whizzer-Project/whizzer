package ro.allevo.connect.resources;

import java.io.Serializable;
import java.util.List;

import ro.allevo.connect.model.BaseQuartz;

public class QuartzPagedApiCollection<T> extends BaseQuartz<T> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private boolean hasMore;
	private List<T> items;
	private int total;
	
	public List<T> getItems() {
		return items;
	}
	public void setItems(List<T> items) {
		this.items = items;
	}
	public boolean getHasMore() {
		if(total > 100) {
			hasMore = true;
		}
		return hasMore;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
}
