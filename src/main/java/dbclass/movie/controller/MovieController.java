package dbclass.movie.controller;

import dbclass.movie.domain.Code;
import dbclass.movie.dto.CodeDTO;
import dbclass.movie.dto.movie.*;
import dbclass.movie.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
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

    //영화관련
    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public MovieDTO registerMovie(@ModelAttribute MovieRegisterDTO movieRegisterDTO) {
        return movieService.register(movieRegisterDTO);
    }

    @GetMapping("/{id}")
    public MovieDTO getMovieDetail(@RequestParam("id") Long movieId) {
        return movieService.getMovie(movieId);
    }

    @Operation(description = "영화 간략 목록 조회(영화 이름, 사진, 등급")
    @GetMapping("/all")
    public List<MovieTitleDTO> getAllMovies() {
        return movieService.getShortMovieList();
    }

    //장르 관련
    @PostMapping(value = "/genre/add")
    public List<Code> addGenre(@RequestBody CodeDTO codeDTO) {
        return movieService.addGenre(codeDTO.getName(), codeDTO.getCode());
    }

    @GetMapping("/genre/list")
    public List<Code> getGenreList() {
        return movieService.loadGenreList();
    }

    @DeleteMapping("/genre/delete")
    public void deleteGenre(@RequestParam("id") String genreId) {
        movieService.deleteGenre(genreId);
//        HttpHeaders headers = new HttpHeaders();
//        headers.setLocation(URI.create("/movie/genre/list"));
//        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    @PostMapping(value = "/genre/modify", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void modifyGenre(@RequestBody CodeDTO codeDTO) {
        movieService.modifyGenre(codeDTO.getName(), codeDTO.getCode());
    }


    //등급관련
    @PostMapping(value = "/rating/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<Code> registerRating(@ModelAttribute RatingDTO ratingDTO) {
        return movieService.updateRating(ratingDTO);
    }

    @PostMapping(value = "/rating/modify", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<Code> modifyRating(@ModelAttribute RatingDTO ratingDTO) {
        return movieService.updateRating(ratingDTO);
    }

    @DeleteMapping("/rating/delete")
    public void deleteRating(@RequestParam("id") String ratingId) {
        movieService.deleteRating(ratingId);
    }

    @GetMapping("/rating/list")
    public List<Code> getRatingList() {
        return movieService.getRatingList();
    }


    //인물 관련
    @PostMapping(value = "/cast/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<CastInMovieDTO> registerCast(@ModelAttribute CastInfoDTO infoDTO) {
        return movieService.addCast(infoDTO);
    }

    @Operation(description = "파라미터에 cast id 보내주기: ex) /cast/detail?castId=1")
    @GetMapping("/cast/detail")
    public CastDTO getCastDetail(@RequestParam("castId") Long castId) {
        return movieService.getCast(castId);
    }

    @PostMapping(value = "/cast/modify", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<CastInMovieDTO> modifyCast(@ModelAttribute CastInfoDTO infoDTO) {
        return movieService.modifyCast(infoDTO);
    }

    @DeleteMapping(value = "/cast/delete")
    public void deleteCast(@RequestParam("castId") Long castId) {
        movieService.deleteCast(castId);
    }

    @GetMapping("/cast/getList")
    public List<CastInMovieDTO> getDirectorShortInfoList() {
        return movieService.getDirectorList();
    }


    //역할 관련
    @PostMapping(value = "/{movieId}/role/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void addRole(@PathVariable("movieId") Long movieId, @ModelAttribute RoleAddDTO rolesToAddDTO) {
        movieService.addRole(movieId, rolesToAddDTO);
    }

    @PostMapping(value = "/{movieId}/role/modify", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void modifyRole(@PathVariable("movieId") Long movieId, @ModelAttribute RoleAddDTO roleAddDTO) {
        movieService.addRole(movieId, roleAddDTO);
    }

    @DeleteMapping("/{movieId}/role/delete")
    public void deleteRole(@PathVariable("movieId") Long movieId, @RequestParam("castId") Long castId) {
        movieService.deleteRole(movieId, castId);
    }

}
