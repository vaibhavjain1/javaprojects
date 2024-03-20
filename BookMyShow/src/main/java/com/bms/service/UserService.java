/**
 * 
 */
package com.bms.service;

import com.bms.dto.UserDto;


public interface UserService {

	UserDto addUser(UserDto userDto);

	UserDto getUser(long id);
}