/**
 * 
 */
package com.bms.service;

import java.time.LocalDate;
import java.time.LocalTime;

import com.bms.dto.PageResponse;
import com.bms.dto.ShowDto;


public interface ShowService {

	ShowDto addShow(ShowDto showDto);

	PageResponse<ShowDto> searchShows(String movieName, String city, LocalDate showDate, LocalTime showTime, int pageNo, int limit);

}