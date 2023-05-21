package dbclass.movie.mapper;

import dbclass.movie.domain.movie.*;
import dbclass.movie.dto.ImageDTO;
import dbclass.movie.dto.movie.*;

import java.sql.Date;

public class MovieMapper {

    private MovieMapper() {}

    public static Movie movieRegisterDTOToMovie(MovieRegisterDTO movieRegisterDTO, Poster poster, Cast cast, Rating rating) {
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

    public static MovieDTO movieToMovieDTO(Movie movie) {
        return MovieDTO.builder()
                .movieId(movie.getMovieId())
                .title(movie.getTitle())
                .releaseDate(movie.getReleaseDate())
                .runningTime(movie.getRunningTime())
                .info(movie.getInfo())
                .countryCode(movie.getCountryCode())
                .language(movie.getLanguage())
                .poster(posterToPosterDTO(movie.getPoster()))
                .director(castToCastInMovieDTO(movie.getDirector()))
                .rating(ratingToRatingDTO(movie.getRating()))
                .build();
    }

    public static RatingDTO ratingToRatingDTO(Rating rating) {
        return RatingDTO.builder()
                .ratingId(rating.getRatingId())
                .name(rating.getName())
                .minAge(rating.getMinAge())
                .build();
    }

    public static Rating ratingDTOToRating(RatingDTO ratingDTO) {
        return Rating.builder()
                .ratingId(ratingDTO.getRatingId())
                .name(ratingDTO.getName())
                .minAge(ratingDTO.getMinAge())
                .build();
    }

    public static Cast castInfoDTOWithImageToCast(CastInfoDTO castInfoDTO, ImageDTO profileImage) {
        return Cast.builder()
                .castId(castInfoDTO.getCastId())
                .name(castInfoDTO.getName())
                .birthDate(Date.valueOf(castInfoDTO.getBirthDate()))
                .nationality(castInfoDTO.getNationality())
                .info(castInfoDTO.getInfo())
                .uuid(profileImage.getUuid())
                .fileName(profileImage.getFileName())
                .fileUrl(profileImage.getFileUrl())
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

    public static GenreDTO genreToGenreDTO(Genre genre) {
        return GenreDTO.builder()
                .genreId(genre.getGenreId())
                .name(genre.getName())
                .build();
    }

    public static MovieTitleDTO movieToMovieTitleDTO(Movie movie) {

        String fileName = movie.getPoster().getUuid() + "_" + movie.getPoster().getFileName();

        return MovieTitleDTO.builder()
                .title(movie.getTitle())
                .movieId(movie.getMovieId())
                .rating(ratingToRatingDTO(movie.getRating()))
                .fileName(fileName)
                .build();
    }

    private static PosterDTO posterToPosterDTO(Poster poster) {
        return PosterDTO.builder()
                .posterId(poster.getPosterId())
                .uuid(poster.getUuid())
                .fileName(poster.getFileName())
                .fileUrl(poster.getFileUrl())
                .build();
    }
}
