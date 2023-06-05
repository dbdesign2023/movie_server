package dbclass.movie.mapper;

import dbclass.movie.domain.Code;
import dbclass.movie.domain.Image;
import dbclass.movie.domain.movie.*;
import dbclass.movie.dto.movie.*;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

public class MovieMapper {

    private MovieMapper() {}

    public static Movie movieRegisterDTOToMovie(MovieRegisterDTO movieRegisterDTO, Image poster, Cast cast, Code rating) {
        return Movie.builder()
                .title(movieRegisterDTO.getTitle())
                .releaseDate(Date.valueOf(movieRegisterDTO.getReleaseDate()))
                .runningTime(movieRegisterDTO.getRunningTime())
                .info(movieRegisterDTO.getInfo())
                .countryCode(movieRegisterDTO.getCountryCode())
                .language(movieRegisterDTO.getLanguage())
                .poster(poster)
                .director(cast)
                .rating(rating)
                .build();
    }

    public static MovieDTO movieToMovieDTO(Movie movie, List<String> genreList) {
        return MovieDTO.builder()
                .movieId(movie.getMovieId())
                .title(movie.getTitle())
                .releaseDate(movie.getReleaseDate())
                .runningTime(movie.getRunningTime())
                .info(movie.getInfo())
                .countryCode(movie.getCountryCode())
                .language(movie.getLanguage())
                .poster(imageToImageDTO(movie.getPoster()))
                .director(castToCastInMovieDTO(movie.getDirector()))
                .rating(movie.getRating().getName())
                .genreList(genreList)
                .build();
    }

    public static Code ratingDTOToCode(RatingDTO ratingDTO, Code upperCode) {
        return Code.builder()
                .code(ratingDTO.getCode())
                .name(ratingDTO.getName())
                .upperCode(upperCode)
                .build();
    }

    public static Cast castInfoDTOWithImageToCast(CastInfoDTO castInfoDTO, Image profileImage) {
        return Cast.builder()
                .castId(castInfoDTO.getCastId())
                .name(castInfoDTO.getName())
                .birthDate(Date.valueOf(castInfoDTO.getBirthDate()))
                .nationality(castInfoDTO.getNationality())
                .info(castInfoDTO.getInfo())
                .profileImage(profileImage)
                .build();
    }

    public static Cast castInfoDTOToCast(CastInfoDTO castInfoDTO) {
        return Cast.builder()
                .castId(castInfoDTO.getCastId())
                .name(castInfoDTO.getName())
                .birthDate(Date.valueOf(castInfoDTO.getBirthDate()))
                .nationality(castInfoDTO.getNationality())
                .info(castInfoDTO.getInfo())
                .build();
    }

    public static CastInMovieDTO castToCastInMovieDTO(Cast cast) {
        return CastInMovieDTO.builder()
                .castId(cast.getCastId())
                .name(cast.getName())
                .build();
    }

    public static Code genreDTOToCode(GenreDTO genreDTO, Code upperCode) {
        return Code.builder()
                .code(genreDTO.getGenreId())
                .name(genreDTO.getName())
                .upperCode(upperCode)
                .build();
    }

    public static MovieTitleWithPosterRatingDTO movieToMovieTitleWithPosterRatingDTO(Movie movie) {

        String fileName = movie.getPoster().getUuid() + "_" + movie.getPoster().getFileName();

        return MovieTitleWithPosterRatingDTO.builder()
                .title(movie.getTitle())
                .movieId(movie.getMovieId())
                .rating(movie.getRating().getName())
                .fileName(fileName)
                .build();
    }

    public static MovieTitleDTO movieToMovieTitleDTO(Movie movie) {
        return MovieTitleDTO.builder()
                .title(movie.getTitle())
                .movieId(movie.getMovieId())
                .build();
    }

    public static CastDTO castToCastDTO(Cast cast) {
        return CastDTO.builder()
                .castId(cast.getCastId())
                .birthDate(cast.getBirthDate())
                .info(cast.getInfo())
                .nationality(cast.getNationality())
                .profileImage(imageToImageDTO(cast.getProfileImage()))
                .build();
    }

    private static ImageDTO imageToImageDTO(Image image) {
        return ImageDTO.builder()
                .imageId(image.getImageId())
                .uuid(image.getUuid())
                .fileName(image.getFileName())
                .fileUrl(image.getFileUrl())
                .build();
    }
}
