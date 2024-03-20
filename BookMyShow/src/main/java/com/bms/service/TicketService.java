/**
 * 
 */
package com.bms.service;

import com.bms.dto.BookTicketRequestDto;
import com.bms.dto.TicketDto;


public interface TicketService {

	TicketDto bookTicket(BookTicketRequestDto bookTicketRequestDto);

	TicketDto getTicket(long id);
}