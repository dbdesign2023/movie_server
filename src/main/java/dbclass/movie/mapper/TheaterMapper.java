package dbclass.movie.mapper;

import dbclass.movie.domain.Code;
import dbclass.movie.domain.theater.Theater;
import dbclass.movie.dto.theater.TheaterDTO;
import dbclass.movie.dto.theater.TheaterRegisterDTO;

public class TheaterMapper {

    private TheaterMapper() {}

    public static Theater theaterRegisterDTOToTheater(TheaterRegisterDTO theaterRegisterDTO, Code code) {
        return Theater.builder()
                .theaterId(theaterRegisterDTO.getTheaterId())
                .type(code)
                .floor(theaterRegisterDTO.getFloor())
                .name(theaterRegisterDTO.getName())
                .build();
    }

    public static TheaterDTO theaterToTheaterDTO(Theater theater) {
        return TheaterDTO.builder()
                .theaterId(theater.getTheaterId())
                .floor(theater.getFloor())
                .name(theater.getName())
                .typeName(theater.getType().getName())
                .typeCode(theater.getType().getCode())
                .build();
    }

}
