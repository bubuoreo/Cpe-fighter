package com.sp.repository;

import org.springframework.data.repository.CrudRepository;

import com.sp.model.Vehicle;


public interface VehicleRepository extends CrudRepository<Vehicle, Integer> {

}
