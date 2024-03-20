/**
 * 
 */
package com.bms.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import com.bms.adapter.TheaterAdapter;
import com.bms.model.TheaterEntity;
import com.bms.model.TheaterSeatsEntity;
import com.bms.service.TheaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bms.dto.TheaterDto;
import com.bms.enums.SeatType;
import com.bms.repository.TheaterRepository;
import com.bms.repository.TheaterSeatsRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class TheaterServiceImpl implements TheaterService {

	@Autowired
	private TheaterRepository theaterRepository;

	@Autowired
	private TheaterSeatsRepository theaterSeatsRepository;

	@Override
	public TheaterDto addTheater(TheaterDto theaterDto) {
		log.info("Adding New Theater: " + theaterDto);

		TheaterEntity theaterEntity = TheaterAdapter.toEntity(theaterDto);

		theaterEntity.getSeats().addAll(getTheaterSeats());

		for (TheaterSeatsEntity seatsEntity : theaterEntity.getSeats()) {
			seatsEntity.setTheater(theaterEntity);
		}

		theaterEntity = theaterRepository.save(theaterEntity);

		log.info("Added New User [id: " + theaterEntity.getId() + ", Name: " + theaterEntity.getName() + "]");

		return TheaterAdapter.toDto(theaterEntity);
	}

	private List<TheaterSeatsEntity> getTheaterSeats() {

		List<TheaterSeatsEntity> seats = new ArrayList<>();

		seats.add(getTheaterSeat("1A", SeatType.CLASSIC, 100));
		seats.add(getTheaterSeat("1B", SeatType.CLASSIC, 100));
		seats.add(getTheaterSeat("1C", SeatType.CLASSIC, 100));
		seats.add(getTheaterSeat("1D", SeatType.CLASSIC, 100));
		seats.add(getTheaterSeat("1E", SeatType.CLASSIC, 100));

		seats.add(getTheaterSeat("2A", SeatType.PREMIUM, 150));
		seats.add(getTheaterSeat("2B", SeatType.PREMIUM, 150));
		seats.add(getTheaterSeat("2C", SeatType.PREMIUM, 150));
		seats.add(getTheaterSeat("2D", SeatType.PREMIUM, 150));
		seats.add(getTheaterSeat("2E", SeatType.PREMIUM, 150));

		seats = theaterSeatsRepository.saveAll(seats);

		log.info("Added " + seats.size() + " Seats for New Theater");

		return seats;
	}

	private TheaterSeatsEntity getTheaterSeat(String seatNumber, SeatType seatType, int rate) {
		return TheaterSeatsEntity.builder().seatNumber(seatNumber).seatType(seatType).rate(rate).build();
	}

	@Override
	public TheaterDto getTheater(long id) {
		log.info("Searching Theater by id: " + id);

		Optional<TheaterEntity> theaterEntity = theaterRepository.findById(id);

		if (!theaterEntity.isPresent()) {
			log.error("Theater not found for id: " + id);
			throw new EntityNotFoundException("Theater Not Found with ID: " + id);
		}

		return TheaterAdapter.toDto(theaterEntity.get());
	}

}