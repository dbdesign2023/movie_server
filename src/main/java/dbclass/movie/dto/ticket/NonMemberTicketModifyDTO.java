package dbclass.movie.dto.ticket;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NonMemberTicketModifyDTO {

    private Long ticketId;
    private String password;
    private String newPassword;
    private List<String> seats;
}