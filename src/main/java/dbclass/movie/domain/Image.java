package dbclass.movie.domain;

import dbclass.movie.domain.movie.Movie;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Table(name = "IMAGE")
@AllArgsConstructor
@ToString
@Getter
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "poster_sequence")
    @SequenceGenerator(name = "poster_sequence", sequenceName = "poster_sequence", allocationSize = 1)
    @Column(name = "IMAGE_ID")
    private Long imageId;

    @Column(name = "UUID", nullable = false)
    private String uuid;
    @Column(name = "FILE_NAME", nullable = false)
    private String fileName;
    @Column(name = "FILE_URL", nullable = false)
    private String fileUrl;
}
