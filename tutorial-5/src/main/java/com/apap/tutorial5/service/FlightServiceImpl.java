package com.apap.tutorial5.service;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apap.tutorial5.model.FlightModel;
import com.apap.tutorial5.repository.FlightDb;

@Service
@Transactional
public class FlightServiceImpl implements FlightService {
	@Autowired
	private FlightDb flightDb;

	@Override
	public void addFlight(FlightModel flight) {
		flightDb.save(flight);
	}

	@Override
	public void deleteFlightById(long id) {
		flightDb.deleteById(id);
	}

	@Override
	public void updateFlight(Long id, String flightNumber, String origin, String destination, Date time) {
		flightDb.getOne(id).setFlightNumber(flightNumber);
		flightDb.getOne(id).setOrigin(origin);
		flightDb.getOne(id).setDestination(destination);
		flightDb.getOne(id).setTime(time);

	}

	@Override
	public FlightModel getFlightDetailById(long id) {
		return flightDb.getOne(id);
		
	}

	@Override
	public List<FlightModel> allFlight() {
		return flightDb.findAll();
		
	}


}
