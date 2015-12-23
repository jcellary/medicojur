package com.medicojur.web.dao;

import java.util.Arrays;
import java.util.List;

public class JournalDao implements Dao<String> {
	@Override
	public List<? extends String> getAll() {
		return Arrays.asList( "Stuff", "Something else", "Whatever" );
	}

	@Override
	public String getById( String id ) {
		return id;
	}
}
