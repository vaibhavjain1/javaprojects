/**
 * 
 */
package com.bms.dto;

import java.util.List;

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
public class PageResponse<T> {

	private int number;
	private int records;

	private long totalRecords;
	private int totalPages;

	private List<T> data;
}