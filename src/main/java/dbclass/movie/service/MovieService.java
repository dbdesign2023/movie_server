package dbclass.movie.service;

import dbclass.movie.domain.Code;
import dbclass.movie.domain.Image;
import dbclass.movie.domain.movie.*;
import dbclass.movie.dto.ImageDTO;
import dbclass.movie.dto.movie.*;
import dbclass.movie.exceptionHandler.*;
import dbclass.movie.mapper.MovieMapper;
import dbclass.movie.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class MovieService {

    private final FileService fileService;
    private final MovieRepository movieRepository;
    private final ImageRepository imageRepository;
    private final CastRepository castRepository;
    private final RoleRepository roleRepository;
    private final GenreRegisterRepository genreRegisterRepository;
    private final CodeRepository codeRepository;
    private final ScheduleRepository scheduleRepository;

    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    @Transactional
    public MovieDTO register(MovieRegisterDTO registerDTO) {
        log.debug("movie register start");
        Cast director = castRepository.findById(registerDTO.getDirectorId()).orElseThrow(() -> new DataNotExistsException("존재하지 않는 감독 ID입니다.", "cast"));
        Code ratingUpperCode = codeRepository.findRatingUpperCode();
        if(!ratingUpperCode.getCode().equals(registerDTO.getRatingCode().substring(0, 3))) {
            throw new InvalidDataException("잘못된 등급 타입입니다.");
        }
        Code rating = codeRepository.findById(registerDTO.getRatingCode()).orElseThrow(() -> new DataNotExistsException("존재하지 않는 등급입니다.", "rating"));
        Image poster = createImage(registerDTO.getPoster(), "poster");

        Movie movie = MovieMapper.movieRegisterDTOToMovie(registerDTO, poster, director, rating);
        log.info(movie);
        Movie movieSaved = movieRepository.save(movie);

        if(registerDTO.getGenreCodes() != null) {
            registerDTO.getGenreCodes().stream().forEach(genreCode -> {
                if(!genreCode.substring(0, 3).equals(codeRepository.findGenreUpperCode().getCode())) {
                    throw new InvalidAccessException("장르 코드가 아닙니다. 다시 확인해주세요.");
                }
                Code genre = codeRepository.findById(genreCode).orElseThrow(() -> new DataNotExistsException("존재하지 않는 장르 코드입니다.", "Genre"));
                genreRegisterRepository.save(GenreRegister.builder().movie(movieSaved).genre(genre).build());
            });
        }

        return MovieMapper.movieToMovieDTO(movie,
                genreRegisterRepository.findAllByMovie(movie)
                        .stream()
                        .map(genreRegister -> genreRegister.getGenre().getName())
                        .collect(Collectors.toList())
                , roleRepository.findAllByMovie(movieSaved).stream().sorted(Comparator.comparing(Role::isStarring).reversed().thenComparing(role -> role.getCast().getName())).collect(Collectors.toList())
        );
    }

    @Transactional
    public MovieDTO modifyMovie(MovieRegisterDTO registerDTO) {
        Movie movie = movieRepository.findById(registerDTO.getMovieId()).orElseThrow(() -> new DataNotExistsException("존재하지 않는 영화입니다.", "MOVIE"));

        log.debug("movie register start");
        Cast director = castRepository.findById(registerDTO.getDirectorId()).orElseThrow(() -> new DataNotExistsException("존재하지 않는 감독 ID입니다.", "cast"));
        Code ratingUpperCode = codeRepository.findRatingUpperCode();
        if(!ratingUpperCode.getCode().equals(registerDTO.getRatingCode().substring(0, 3))) {
            throw new InvalidDataException("잘못된 등급 타입입니다.");
        }
        Code rating = codeRepository.findById(registerDTO.getRatingCode()).orElseThrow(() -> new DataNotExistsException("존재하지 않는 등급입니다.", "rating"));
        Image poster = movie.getPoster();
        if(registerDTO.getPoster() != null) {
            createImage(registerDTO.getPoster(), "poster");
        }

        genreRegisterRepository.deleteAllByMovie(movie);

        movie = MovieMapper.movieRegisterDTOToMovie(registerDTO, poster, director, rating);


        Movie movieSaved = movieRepository.save(movie);

        if(registerDTO.getGenreCodes() != null) {
            registerDTO.getGenreCodes().stream().forEach(genreCode -> {
                if(!genreCode.substring(0, 3).equals(codeRepository.findGenreUpperCode().getCode())) {
                    throw new InvalidAccessException("장르 코드가 아닙니다. 다시 확인해주세요.");
                }
                Code genre = codeRepository.findById(genreCode).orElseThrow(() -> new DataNotExistsException("존재하지 않는 장르 코드입니다.", "Genre"));
                genreRegisterRepository.save(GenreRegister.builder().movie(movieSaved).genre(genre).build());
            });
        }

        return MovieMapper.movieToMovieDTO(movie,
                genreRegisterRepository.findAllByMovie(movie)
                        .stream()
                        .map(genreRegister -> genreRegister.getGenre().getName())
                        .collect(Collectors.toList())
                , roleRepository.findAllByMovie(movieSaved).stream().sorted(Comparator.comparing(Role::isStarring).reversed().thenComparing(role -> role.getCast().getName())).collect(Collectors.toList())
        );

    }

    private Image createImage(MultipartFile file, String target) {
        String originalName = file.getOriginalFilename();
        Path root = Paths.get(uploadPath, target);

        try {
            ImageDTO imageDTO =  fileService.createImageDTO(originalName, root);
            Image poster = Image.builder()
                    .uuid(imageDTO.getUuid())
                    .fileName(imageDTO.getFileName())
                    .fileUrl(imageDTO.getFileUrl())
                    .build();

            file.transferTo(Paths.get(imageDTO.getFileUrl()));

            return imageRepository.save(poster);
        } catch (IOException e) {
            throw new ServerException("파일 저장 실패");
        }
    }

    @Transactional
    public List<Code> addRating(RatingDTO ratingDTO) {

        if(codeRepository.existsByName(ratingDTO.getName())) {
            throw new DataExistsException("존재하는 rating 이름입니다.", "rating");
        }
        if(codeRepository.existsById(ratingDTO.getCode())) {
            throw new DataExistsException("존재하는 rating ID입니다.", "rating");
        }

        Code upperCode = codeRepository.findRatingUpperCode();
        if(!ratingDTO.getCode().substring(0, 3).equals(upperCode.getCode())) {
            throw new InvalidDataException("잘못된 코드 형식입니다. 장르는 GR0 형식으로 이루어져야 합니다.");
        }

        codeRepository.save(MovieMapper.ratingDTOToCode(ratingDTO, upperCode));

        return getRatingList();
    }

    @Transactional
    public List<Code> updateRating(RatingDTO ratingDTO) {

        if(codeRepository.existsByName(ratingDTO.getName())) {
            throw new DataExistsException("존재하는 rating 이름입니다.", "rating");
        }

        if(!codeRepository.existsById(ratingDTO.getCode())) {
            throw new DataNotExistsException("존재하지 않는 rating ID입니다.", "rating");
        }

        Code upperCode = codeRepository.findRatingUpperCode();
        if(!ratingDTO.getCode().substring(0, 3).equals(upperCode.getCode())) {
            throw new InvalidDataException("잘못된 코드 형식입니다. 장르는 GR0 형식으로 이루어져야 합니다.");
        }

        codeRepository.save(MovieMapper.ratingDTOToCode(ratingDTO, upperCode));

        return getRatingList();
    }

    @Transactional(readOnly = true)
    public List<Code> getRatingList() {
        return codeRepository.findAllByUpperCode(codeRepository.findRatingUpperCode()).stream().sorted(Comparator.comparing(Code::getCode)).collect(Collectors.toList());
    }


    //같은 인물 저장되어 있는지 확인할 방법은?
    @Transactional
    public List<CastInMovieDTO> addCast(CastInfoDTO infoDTO) {
        Image profileImage = createImage(infoDTO.getProfileImage(), "cast");

        castRepository.save(MovieMapper.castInfoDTOWithImageToCast(infoDTO, profileImage));
        return getDirectorList();
    }

    @Transactional
    public List<CastInMovieDTO> modifyCast(CastInfoDTO infoDTO) {
        if(infoDTO.getProfileImage() == null) {
            castRepository.updateWithoutImage(MovieMapper.castInfoDTOToCast(infoDTO));
            return getDirectorList();
        }

        Image profileImage = createImage(infoDTO.getProfileImage(), "cast");

        Cast cast = MovieMapper.castInfoDTOWithImageToCast(infoDTO, profileImage);
        castRepository.save(cast);
        return getDirectorList();
    }

    @Transactional(readOnly = true)
    public List<CastInMovieDTO> getDirectorList() {
        List<Cast> directorList = castRepository.findAll();
        return directorList.stream().sorted(Comparator.comparing(Cast::getName)).map(cast -> MovieMapper.castToCastInMovieDTO(cast)).collect(Collectors.toList());
    }

    @Transactional
    public void addRole(Long movieId, RoleAddDTO roleAddDTO) {
        Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new DataNotExistsException("존재하지 않는 영화 ID입니다.", "movie"));
        Cast cast = castRepository.findById(roleAddDTO.getCastId()).orElseThrow(() -> new DataNotExistsException("존재하지 않는 배우 ID입니다.", "cast"));

        RoleId roleId = RoleId.builder().movie(movie).cast(cast).build();
        if(roleRepository.existsById(roleId)) {
            roleRepository.updateRoleById(roleId, roleAddDTO.getRole(), roleAddDTO.isStarring());
            return;
        }

        Role role = Role.builder()
                .movie(movie)
                .cast(cast)
                .role(roleAddDTO.getRole())
                .starring(roleAddDTO.isStarring())
                .build();

        roleRepository.save(role);
    }

    @Transactional
    public void modifyGenre(String name, String code) {
        if(codeRepository.existsByName(name)) {
            throw new DataExistsException("이미 저장된 장르 입니다.", "genre");
        }

        if(!codeRepository.existsById(code)) {
            throw new DataNotExistsException("존재하지 않는 장르ID 입니다.", "genre");
        }

        Code upperCode = codeRepository.findGenreUpperCode();

        if(!code.substring(0, 3).equals(upperCode.getCode())) {
            throw new InvalidDataException("잘못된 코드 형식입니다. 장르는 GR0 형식으로 이루어져야 합니다.");
        }

        Code genre = Code.builder()
                .code(code)
                .name(name)
                .upperCode(upperCode)
                .build();

        codeRepository.save(genre);
    }

    @Transactional
    public void deleteGenre(String codeId) {
        codeRepository.deleteById(codeId);
    }

    @Transactional
    public List<Code> addGenre(String name, String code) {
        if(codeRepository.existsByName(name)) {
            throw new DataExistsException("이미 저장된 장르 입니다.", "genre");
        }

        if(codeRepository.existsById(code)) {
            throw new DataExistsException("이미 사용중인 장르ID 입니다.", "genre");
        }

        Code upperCode = codeRepository.findGenreUpperCode();

        if(!code.substring(0, 3).equals(upperCode.getCode())) {
            throw new InvalidDataException("잘못된 코드 형식입니다. 장르는 GR0 형식으로 이루어져야 합니다.");
        }
        Code genre = Code.builder()
                .code(code)
                .name(name)
                .upperCode(upperCode)
                .build();

        codeRepository.save(genre);

        return loadGenreList();
    }

    @Transactional(readOnly = true)
    public List<Code> loadGenreList() {
        return codeRepository.findAllByUpperCode(codeRepository.findGenreUpperCode()).stream().sorted(Comparator.comparing(Code::getCode)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MovieDTO getMovie(Long movieId) {
        Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new DataNotExistsException("존재하지 않는 영화 ID입니다.", "movie"));

        return MovieMapper.movieToMovieDTO(movie,
                genreRegisterRepository.findAllByMovie(movie).stream().map(genreRegister -> genreRegister.getGenre().getName()).collect(Collectors.toList())
                , roleRepository.findAllByMovie(movie).stream().sorted(Comparator.comparing(Role::isStarring).reversed().thenComparing(role -> role.getCast().getName())).collect(Collectors.toList()));
    }

    @Transactional
    public void deleteCast(Long castId) {
        Cast cast = castRepository.findById(castId).orElseThrow(() -> new DataNotExistsException("존재하지 않는 배우 ID입니다", "CAST"));
        castRepository.deleteById(castId);
        imageRepository.deleteById(cast.getProfileImage().getImageId());
    }

    @Transactional
    public void deleteRole(Long movieId, Long castId) {
        RoleId roleId = RoleId.builder()
                .cast(castRepository.findById(castId).orElseThrow(() -> new DataNotExistsException("존재하지 않는 배우 ID입니다.", "cast")))
                .movie(movieRepository.findById(movieId).orElseThrow(() -> new DataNotExistsException("존재하지 않는 영화 ID입니다.", "movie")))
                .build();
        roleRepository.deleteById(roleId);
    }

    @Transactional
    public void deleteRating(String ratingCode) {
        if(movieRepository.existsByRating(codeRepository.findById(ratingCode).orElseThrow(() -> new DataNotExistsException("존재하지 않는 등급 ID입니다.", "Rating")))) {
            throw new DataExistsException("해당 등급을 가진 영화가 존재합니다.", "RATING");
        }

        else {
            codeRepository.deleteById(ratingCode);
        }
    }

    @Transactional(readOnly = true)
    public CastDTO getCast(Long castId) {
        return castRepository.findById(castId).map(cast -> MovieMapper.castToCastDTO(cast)).orElseThrow(() -> new DataNotExistsException("존재하지 않는 인물 ID입니다.", "CAST"));
    }

    @Transactional(readOnly = true)
    public List<MovieTitleDTO> getShortMovieList() {
        return movieRepository.findAll().stream().sorted(Comparator.comparing(Movie::getReleaseDate).reversed().thenComparing(Movie::getTitle)).distinct().map(movie -> MovieMapper.movieToMovieTitleDTO(movie)).collect(Collectors.toList());
    }

    @Transactional
    public void deleteMovie(Long movieId) {
        if(scheduleRepository.existsByMovieId(movieId)) {
            throw new DataExistsException("이미 등록된 상영일정이 존재하여 영화 삭제가 불가합니다. 상영 일정을 삭제 후 다시 진행해주세요.", "Movie");
        }

        log.info(movieId);

        Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new DataNotExistsException("존재하지 않는 영화입니다.", "Movie"));

        genreRegisterRepository.deleteAllByMovie(movie);

        movieRepository.deleteById(movieId);

        imageRepository.deleteById(movie.getPoster().getImageId());
    }
//
//    @Transactional
//    public void addGenreToMovie()
}
