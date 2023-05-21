package dbclass.movie.mapper;

import dbclass.movie.domain.theater.Seat;
import dbclass.movie.domain.theater.Theater;
import dbclass.movie.dto.theater.SeatDTO;
import dbclass.movie.dto.theater.SeatRegisterDTO;
import dbclass.movie.dto.theater.TheaterDTO;
import dbclass.movie.dto.theater.TheaterRegisterDTO;

public class TheaterMapper {

    private TheaterMapper() {}

    public static Theater theaterRegisterDTOToTheater(TheaterRegisterDTO theaterRegisterDTO) {
        return Theater.builder()
                .theaterId(theaterRegisterDTO.getTheaterId())
                .type(theaterRegisterDTO.getType())
                .floor(theaterRegisterDTO.getFloor())
                .name(theaterRegisterDTO.getName())
                .build();
    }

    public static TheaterDTO theaterToTheaterDTO(Theater theater) {
        return TheaterDTO.builder()
                .theaterId(theater.getTheaterId())
                .floor(theater.getFloor())
                .name(theater.getName())
                .type(theater.getType())
                .build();
    }

}
