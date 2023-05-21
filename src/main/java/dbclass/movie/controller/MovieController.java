package dbclass.movie.controller;

import dbclass.movie.dto.movie.*;
import dbclass.movie.service.MovieService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/movie")
@Tag(name = "영화 관련 로직", description = "영화 페이지 등록 시 영화/장르/등급/캐스트/역할 추가 API")
public class MovieController {

    private final MovieService movieService;

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public MovieDTO registerMovie(@ModelAttribute MovieRegisterDTO movieRegisterDTO) {
        return movieService.register(movieRegisterDTO);
    }

    @PostMapping(value = "/rating/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<RatingDTO> registerRating(@ModelAttribute RatingDTO ratingDTO) {
        return movieService.updateRating(ratingDTO);
    }

    @PostMapping(value = "/rating/modify", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<RatingDTO> modifyRating(@ModelAttribute RatingDTO ratingDTO) {
        return movieService.updateRating(ratingDTO);
    }

    @GetMapping("/rating/list")
    public List<RatingDTO> getRatingList() {
        return movieService.getRatingList();
    }

    @PostMapping(value = "/{movieId}/role/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void addRole(@PathVariable("movieId") Long movieId, @ModelAttribute RoleAddDTO rolesToAddDTO) {
        movieService.addRole(movieId, rolesToAddDTO);
    }

    @PostMapping(value = "/cast/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<CastInMovieDTO> registerCast(@ModelAttribute CastInfoDTO infoDTO) {
        return movieService.addCast(infoDTO);
    }

    @PostMapping(value = "/cast/modify", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<CastInMovieDTO> modifyCast(@ModelAttribute CastInfoDTO infoDTO) {
        return movieService.modifyCast(infoDTO);
    }

    @GetMapping("/cast/getList")
    public List<CastInMovieDTO> getDirectorShortInfoList() {
        return movieService.getDirectorList();
    }

    @PostMapping("/genre/add")
    public List<GenreDTO> addGenre(@RequestBody String name) {
        return movieService.addGenre(name);
    }

    @GetMapping("/genre/list")
    public List<GenreDTO> getGenreList() {
        return movieService.loadGenreList();
    }

//    @GetMapping("/{id}")
//    public MovieDTO getMovieDetail(@RequestParam("id") Long movieId) {
//        MovieDTO movieDTO = movieService.getMovie(movieId);
//    }

}
