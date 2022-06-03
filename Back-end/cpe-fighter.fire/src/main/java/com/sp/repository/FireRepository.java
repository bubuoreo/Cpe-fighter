package com.sp.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sp.model.Fire;

@Repository
public interface FireRepository  extends CrudRepository<Fire, Integer> {
	
	public Iterable<Fire> findAll();
}