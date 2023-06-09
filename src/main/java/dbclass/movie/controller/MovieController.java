package dbclass.movie.controller;

import dbclass.movie.domain.Code;
import dbclass.movie.dto.CodeDTO;
import dbclass.movie.dto.movie.*;
import dbclass.movie.exceptionHandler.InvalidAccessException;
import dbclass.movie.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
        log.info("movie register request: " + movieRegisterDTO);
        return movieService.register(movieRegisterDTO);
    }

    @GetMapping("/detail")
    public MovieDTO getMovieDetail(@RequestParam("id") Long movieId) {
        log.info("movie detail request: " + movieId);
        return movieService.getMovie(movieId);
    }

    @Operation(description = "영화 간략 목록 조회(영화 이름, 사진, 등급")
    @GetMapping("/all")
    public List<MovieTitleDTO> getAllMovies() {
        log.info("movie list request");
        return movieService.getShortMovieList();
    }

    @PostMapping(value = "/modify", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public MovieDTO modifyMovie(@ModelAttribute MovieRegisterDTO movieRegisterDTO) {
        log.info("movie modify request: " + movieRegisterDTO);
        return movieService.modifyMovie(movieRegisterDTO);
    }

    @DeleteMapping("/delete")
    public void deleteMovie(@RequestParam("id") Long movieId) {
        log.info("movie delete request: " + movieId);
        movieService.deleteMovie(movieId);
    }

    //장르 관련
    @PostMapping(value = "/genre/add")
    public List<Code> addGenre(@Valid @RequestBody CodeDTO codeDTO) {
        log.info("genre add request: " + codeDTO);
        if(codeDTO.getCode() == null || codeDTO.getName() == null) {
            throw new InvalidAccessException("데이터가 비어있습니다.");
        }
        return movieService.addGenre(codeDTO.getName(), codeDTO.getCode());
    }

    @GetMapping("/genre/list")
    public List<Code> getGenreList() {
        log.info("load genre list");
        return movieService.loadGenreList();
    }

    @DeleteMapping("/genre/delete")
    public void deleteGenre(@RequestParam("id") String genreId) {
        log.info("delete genre: " + genreId);
        movieService.deleteGenre(genreId);
//        HttpHeaders headers = new HttpHeaders();
//        headers.setLocation(URI.create("/movie/genre/list"));
//        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    @PostMapping(value = "/genre/modify")
    public void modifyGenre(@Valid @RequestBody CodeDTO codeDTO) {
        log.info("modify genre: " + codeDTO);
        if(codeDTO.getCode() == null || codeDTO.getName() == null) {
            throw new InvalidAccessException("데이터가 비어있습니다.");
        }
        movieService.modifyGenre(codeDTO.getName(), codeDTO.getCode());
    }


    //등급관련
    @PostMapping(value = "/rating/add")
    public List<Code> registerRating(@Valid @RequestBody RatingDTO ratingDTO) {
        log.info("add rating request: " + ratingDTO);
        if(ratingDTO.getCode() == null || ratingDTO.getName() == null) {
            throw new InvalidAccessException("데이터가 비어있습니다.");
        }
        return movieService.addRating(ratingDTO);
    }

    @PostMapping(value = "/rating/modify")
    public List<Code> modifyRating(@Valid @RequestBody RatingDTO ratingDTO) {
        log.info("rating modify request: " + ratingDTO);
        if(ratingDTO.getCode() == null || ratingDTO.getName() == null) {
            throw new InvalidAccessException("데이터가 비어있습니다.");
        }
        return movieService.updateRating(ratingDTO);
    }

    @DeleteMapping("/rating/delete")
    public void deleteRating(@RequestParam("id") String ratingId) {
        log.info("rating delete request: " + ratingId);
        movieService.deleteRating(ratingId);
    }

    @GetMapping("/rating/list")
    public List<Code> getRatingList() {
        log.info("rating list");
        return movieService.getRatingList();
    }


    //인물 관련
    @PostMapping(value = "/cast/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<CastInMovieDTO> registerCast(@ModelAttribute CastInfoDTO infoDTO) {
        log.info("cast register request: " + infoDTO);
        return movieService.addCast(infoDTO);
    }

    @Operation(description = "파라미터에 cast id 보내주기: ex) /cast/detail?castId=1")
    @GetMapping("/cast/detail")
    public CastDTO getCastDetail(@RequestParam("castId") Long castId) {
        log.info("cast detail: " + castId);
        return movieService.getCast(castId);
    }

    @PostMapping(value = "/cast/modify")
    public List<CastInMovieDTO> modifyCast(@ModelAttribute CastInfoDTO infoDTO) {
        log.info("cast modify request: " + infoDTO);
        return movieService.modifyCast(infoDTO);
    }

    @DeleteMapping(value = "/cast/delete")
    public void deleteCast(@RequestParam("castId") Long castId) {
        log.info("cast delete request: " + castId);
        movieService.deleteCast(castId);
    }

    @GetMapping("/cast/getList")
    public List<CastInMovieDTO> getDirectorShortInfoList() {
        log.info("cast list");
        return movieService.getDirectorList();
    }


    //역할 관련
    @PostMapping(value = "/{movieId}/role/add")
    public void addRole(@PathVariable("movieId") Long movieId, @RequestBody RoleAddDTO rolesToAddDTO) {
        log.info("add role request: " + rolesToAddDTO);
        movieService.addRole(movieId, rolesToAddDTO);
    }

    @PostMapping(value = "/{movieId}/role/modify")
    public void modifyRole(@PathVariable("movieId") Long movieId, @RequestBody RoleAddDTO roleAddDTO) {
        log.info("role modify request: " + roleAddDTO);
        movieService.addRole(movieId, roleAddDTO);
    }

    @DeleteMapping("/{movieId}/role/delete")
    public void deleteRole(@PathVariable("movieId") Long movieId, @RequestParam("castId") Long castId) {
        log.info("role delete request: " + castId);
        movieService.deleteRole(movieId, castId);
    }

}
