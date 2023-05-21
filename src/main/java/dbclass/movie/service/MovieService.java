package dbclass.movie.service;

import dbclass.movie.domain.movie.*;
import dbclass.movie.dto.ImageDTO;
import dbclass.movie.dto.movie.*;
import dbclass.movie.exceptionHandler.DataExistsException;
import dbclass.movie.exceptionHandler.DataNotExistsException;
import dbclass.movie.exceptionHandler.ServerException;
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
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class MovieService {

    private final FileService fileService;
    private final MovieRepository movieRepository;
    private final PosterRepository posterRepository;
    private final CastRepository castRepository;
    private final RatingRepository ratingRepository;
    private final RoleRepository roleRepository;
    private final GenreRepository genreRepository;

    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    @Transactional
    public MovieDTO register(MovieRegisterDTO registerDTO) {
        log.debug("movie register start");
        Cast director = castRepository.findById(registerDTO.getDirectorId()).orElseThrow(() -> new DataNotExistsException("존재하지 않는 감독 ID입니다.", "cast"));
        Rating rating = ratingRepository.findById(registerDTO.getRatingId()).orElseThrow(() -> new DataNotExistsException("존재하지 않는 등급입니다.", "rating"));
        Poster poster = createPoster(registerDTO.getPoster());

        Movie movie = MovieMapper.movieRegisterDTOToMovie(registerDTO, poster, director, rating);
        movie = movieRepository.save(movie);

        List<RoleAddDTO> rolesToAddDTO = registerDTO.getCastRoles();
        if(rolesToAddDTO != null) {
            ListIterator list = rolesToAddDTO.listIterator();
            while (list.hasNext()) {
                addRole(movie.getMovieId(), (RoleAddDTO) list.next());
            }
        }

        return MovieMapper.movieToMovieDTO(movie);
    }

    @Transactional
    private Poster createPoster(MultipartFile file) {
        String originalName = file.getOriginalFilename();
        Path root = Paths.get(uploadPath, "poster");

        try {
            ImageDTO imageDTO =  fileService.createImageDTO(originalName, root);
            Poster poster = Poster.builder()
                    .uuid(imageDTO.getUuid())
                    .fileName(imageDTO.getFileName())
                    .fileUrl(imageDTO.getFileUrl())
                    .build();

            file.transferTo(Paths.get(imageDTO.getFileUrl()));

            return posterRepository.save(poster);
        } catch (IOException e) {
            throw new ServerException("파일 저장 실패");
        }
    }

    @Transactional
    public List<RatingDTO> updateRating(RatingDTO ratingDTO) {

        if(ratingRepository.existsByName(ratingDTO.getName())) {
            throw new DataExistsException("존재하는 rating 이름입니다.", "rating");
        }

        ratingRepository.save(MovieMapper.ratingDTOToRating(ratingDTO));

        return getRatingList();
    }

    @Transactional(readOnly = true)
    public List<RatingDTO> getRatingList() {
        return ratingRepository.findAll().stream().map(rating -> MovieMapper.ratingToRatingDTO(rating)).collect(Collectors.toList());
    }


    //같은 인물 저장되어 있는지 확인할 방법은?
    @Transactional
    public List<CastInMovieDTO> addCast(CastInfoDTO infoDTO) {
        ImageDTO imageDTO = createCastImageDTO(infoDTO.getProfileImage());

        castRepository.save(MovieMapper.castInfoDTOWithImageToCast(infoDTO, imageDTO));
        return getDirectorList();
    }

    private ImageDTO createCastImageDTO(MultipartFile file) {
        String originalName = file.getOriginalFilename();
        Path root = Paths.get(uploadPath, "cast");

        try {
            ImageDTO imageDTO =  fileService.createImageDTO(originalName, root);
            file.transferTo(Paths.get(imageDTO.getFileUrl()));

            return imageDTO;
        } catch (IOException e) {
            throw new ServerException("파일 저장 실패");
        }
    }

    @Transactional
    public List<CastInMovieDTO> modifyCast(CastInfoDTO infoDTO) {
        if(infoDTO.getProfileImage().isEmpty()) {
            castRepository.updateWithoutImage(MovieMapper.castInfoDTOToCast(infoDTO));
            return getDirectorList();
        }

        ImageDTO imageDTO = createCastImageDTO(infoDTO.getProfileImage());

        Cast cast = MovieMapper.castInfoDTOWithImageToCast(infoDTO, imageDTO);
        castRepository.save(cast);
        return getDirectorList();
    }

    @Transactional(readOnly = true)
    public List<CastInMovieDTO> getDirectorList() {
        List<Cast> directorList = castRepository.findAll();
        return directorList.stream().map(cast -> MovieMapper.castToCastInMovieDTO(cast)).collect(Collectors.toList());
    }

    @Transactional
    public void addRole(Long movieId, RoleAddDTO roleAddDTO) {
        Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new DataNotExistsException("존재하지 않는 영화 ID입니다.", "movie"));
        Cast cast = castRepository.findById(roleAddDTO.getCastId()).orElseThrow(() -> new DataNotExistsException("존재하지 않는 배우 ID입니다.", "cast"));

        Role role = Role.builder()
                .movie(movie)
                .cast(cast)
                .role(roleAddDTO.getRole())
                .starring(roleAddDTO.isStarring())
                .build();

        roleRepository.save(role);
    }

    @Transactional
    public List<GenreDTO> addGenre(String name) {
        if(genreRepository.existsByName(name)) {
            throw new DataExistsException("이미 저장된 장르 입니다.", "genre");
        }

        Genre genre = Genre.builder()
                .name(name)
                .build();

        genreRepository.save(genre);

        return loadGenreList();
    }

    @Transactional(readOnly = true)
    public List<GenreDTO> loadGenreList() {
        return genreRepository.findAll().stream().map(genre -> MovieMapper.genreToGenreDTO(genre)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MovieDTO getMovie(Long movieId) {
        Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new DataNotExistsException("존재하지 않는 영화 ID입니다.", "movie"));

        return MovieMapper.movieToMovieDTO(movie);
    }

    @Transactional(readOnly = true)
    public boolean hasPoster(String path) {
        return posterRepository.existsByFileUrl(path);
    }

//
//    @Transactional
//    public void addGenreToMovie()
}
