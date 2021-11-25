package ro.allevo.fintpui.utils;

import java.lang.reflect.Array;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PagedCollection<T> {

	private boolean hasMore;

	private int total;

	private T[] items;

	@JsonGetter(value = "hasMore")
	public boolean hasMore() {
		return hasMore;
	}

	public int getTotal() {
		return total;
	}

	public T[] getItems() {
		return items;
	}

	@SuppressWarnings("unchecked")
	public void setItem(T[] addItems) {
		Class<?> itemsCompType = items.getClass().getComponentType();
		Class<?> additemsCompType = addItems.getClass().getComponentType();
		if (itemsCompType.equals(additemsCompType)) {

			int itemsLen = items.length;
			int addItemsLen = addItems.length;

			T[] result = (T[]) Array.newInstance(itemsCompType, itemsLen + addItemsLen);

			System.arraycopy(items, 0, result, 0, itemsLen);
			System.arraycopy(addItems, 0, result, itemsLen, addItemsLen);

			this.items = result;
		}
	}
}
