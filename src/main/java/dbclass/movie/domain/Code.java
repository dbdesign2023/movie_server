package dbclass.movie.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Table(name = "CODE")
@AllArgsConstructor
@ToString
@Getter
public class Code {

    @Id
    @Column(name = "CODE")
    private String code;

    @Column(name = "NAME")
    private String name;

    @ManyToOne
    @JoinColumn(name = "UPPER_CODE")
    private Code upperCode;

}
