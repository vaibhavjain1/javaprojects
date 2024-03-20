/**
 * 
 */
package com.bms.service;

import com.bms.dto.TheaterDto;


public interface TheaterService {

	TheaterDto addTheater(TheaterDto theaterDto);

	TheaterDto getTheater(long id);
}