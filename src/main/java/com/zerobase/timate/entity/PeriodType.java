package com.zerobase.timate.entity;

import static com.zerobase.timate.type.ErrorCode.INVALID_PERIOD;

import com.zerobase.timate.exception.ScheduleException;

public enum PeriodType {
	DAILY,
	WEEKLY,
	MONTHLY;

	public static PeriodType fromString(String period) {
		try{
			return PeriodType.valueOf(period.toUpperCase());
		} catch (Exception e){
			throw new ScheduleException(INVALID_PERIOD);
		}
	}
}
