/**
 * 
 */
package com.bms.service;

import com.bms.dto.MovieDto;


public interface MovieService {

	MovieDto addMovie(MovieDto movieDto);

	MovieDto getMovie(long id);
}