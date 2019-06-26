package com.jhyx.halfroom.service;

public interface PhoneService {
	Boolean sendLoginCode(String phone);

	String getPhoneCodeByPhone(String phone);
}
