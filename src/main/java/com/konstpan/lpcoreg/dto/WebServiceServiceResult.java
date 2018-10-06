package com.konstpan.lpcoreg.dto;

import java.util.ArrayList;
import java.util.List;

public class WebServiceServiceResult<T> extends ServiceResult {

	private static final long serialVersionUID = 1L;

	public WebServiceServiceResult() {
		super();
	}

	T item;

	ArrayList<T> items;

	public T getItem() {
		return item;
	}

	public void setItem(T item) {
		this.item = item;
	}

	public List<T> getItems() {
		if(items == null) {
			items = new ArrayList<>();
		}
		return items;
	}

	public void setItems(List<T> items) {
		this.items = new ArrayList<>(items);
	}

}
