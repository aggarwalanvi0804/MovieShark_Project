package com.project.movieshark.resource;

import lombok.*;

import java.util.Date;

import com.project.movieshark.enums.SeatType;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString
public class ShowSeatsResource {

	private long id;

	private String seatNumber;

	private int rate;

	private SeatType seatType;

	private boolean booked;

	private Date bookedAt;

}