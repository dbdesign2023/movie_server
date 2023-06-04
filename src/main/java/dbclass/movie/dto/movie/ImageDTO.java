package dbclass.movie.dto.movie;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ImageDTO {

    private Long imageId;
    private String uuid;
    private String fileName;
    private String fileUrl;
}
