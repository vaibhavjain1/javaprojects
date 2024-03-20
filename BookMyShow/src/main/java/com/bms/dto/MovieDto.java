/**
 * 
 */
package com.bms.dto;

import java.time.LocalDate;

import com.bms.enums.Genre;
import com.bms.enums.MovieLanguage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString
public class MovieDto {

	private long id;

	private String name;

	private MovieLanguage language;

	private Genre genre;

	private LocalDate releaseDate;

}