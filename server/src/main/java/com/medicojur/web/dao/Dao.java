package com.medicojur.web.dao;

import java.util.List;

public interface Dao<T> {

	List<? extends T> getAll();

	T getById(String id);

}
