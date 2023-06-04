package dbclass.movie.domain.movie;

import dbclass.movie.domain.Image;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "CAST")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class Cast {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cast_sequence")
    @SequenceGenerator(name = "cast_sequence", sequenceName = "cast_sequence", allocationSize = 1)
    @Column(name = "CAST_ID")
    private Long castId;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "BIRTHDATE", nullable = false)
    private Date birthDate;

    @Column(name = "NATIONALITY", nullable = false)
    private String nationality;

    @Column(name = "INFO", nullable = false)
    private String info;

    @OneToMany(mappedBy = "cast", cascade = CascadeType.ALL)
    private List<Role> roles;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROFILE_IMAGE")
    private Image profileImage;

}
