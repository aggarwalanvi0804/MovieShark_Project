package com.project.movieshark.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.project.movieshark.domain.*;
import com.project.movieshark.exception.NotFoundException;
import com.project.movieshark.repository.MovieRepository;
import com.project.movieshark.repository.ShowRepository;
import com.project.movieshark.repository.ShowSeatsRepository;
import com.project.movieshark.repository.TheaterRepository;
import com.project.movieshark.resource.ShowResource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ShowService {

	Logger log=LoggerFactory.getLogger(ShowService.class);

	@Autowired
	private ShowRepository showsRepository;

	@Autowired
	private MovieRepository movieRepository;

	@Autowired
	private TheaterRepository theaterRepository;

	@Autowired
	private ShowSeatsRepository showSeatsRepository;

	public ShowService() {
	}


	public ShowResource addShow(ShowResource showResource) {

		Optional<Movie> optionalMovie = movieRepository.findById(showResource.getMovieId());

		if (!optionalMovie.isPresent()) {
			throw new NotFoundException("Movie Not Found with ID: " + showResource.getMovieId() + " to add New Show");
		}

		Optional<Theater> optionalTheater = theaterRepository.findById(showResource.getTheaterId());

		if (!optionalTheater.isPresent()) {
			throw new NotFoundException("Theater Not Found with ID: " + showResource.getTheaterId() + " to add New Show");
		}

		log.info("Adding New Show: " + showResource);

		Show show = Show.toEntity(showResource);

		show.setMovie(optionalMovie.get());
		show.setTheater(optionalTheater.get());
		show.setSeats(generateShowSeats(show.getTheater().getSeats(), show));

		for (ShowSeat seatsEntity : show.getSeats()) {
			seatsEntity.setShow(show);
		}

		show = showsRepository.save(show);

		return Show.toResource(show);
	}

	private List<ShowSeat> generateShowSeats(List<TheaterSeats> theaterSeatsEntities, Show show) {

		List<ShowSeat> showSeatsEntities = new ArrayList<>();

		for (TheaterSeats theaterSeats : theaterSeatsEntities) {

			ShowSeat showSeat =
					ShowSeat.builder()
							.seatNumber(theaterSeats.getSeatNumber())
							.seatType(theaterSeats.getSeatType())
							.rate(100) // add logic to update seat rate here
							.build();

			showSeatsEntities.add(showSeat);
		}

		return showSeatsRepository.saveAll(showSeatsEntities);
	}


	public List<ShowResource> searchShows(String movieName,String cityName,String theaterName) {

		if(!StringUtils.hasText(cityName))
			new ArrayList<>();
		List<Show> shows=new ArrayList<>();
		if(StringUtils.hasText(movieName))
			shows=showsRepository.findByMovieNameAndCity(movieName,cityName);
		else if(StringUtils.hasText(theaterName)){
			shows=showsRepository.findByTheaterAndCity(theaterName,cityName);
		}
		else{
			shows=showsRepository.findByCity(cityName);
		}
		if(CollectionUtils.isEmpty(shows))
			return new ArrayList<>();
		else
			return shows.stream().map(Show::toResource).collect(Collectors.toList());
	}

}