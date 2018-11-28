package com.asiainfo.commons.model;

import java.util.List;
/**
 * 翻页实体类
 * @author Administrator
 *
 * @param <T>
 */
public class PaginationVO<T> {
	public List<T> dataList;
	private long count;

	public List<T> getDataList() {
		return dataList;
	}

	public void setDataList(List<T> dataList) {
		this.dataList = dataList;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

}
