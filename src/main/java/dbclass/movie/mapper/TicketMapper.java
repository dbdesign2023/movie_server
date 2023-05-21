package dbclass.movie.mapper;

import dbclass.movie.domain.schedule.Schedule;
import dbclass.movie.domain.ticket.Ticketing;
import dbclass.movie.domain.user.Customer;
import dbclass.movie.dto.ticket.TicketReserveDTO;

public class TicketMapper {

    private TicketMapper() {}

    public static Ticketing TicketReserveDTOToTicket(TicketReserveDTO ticketReserveDTO, Customer customer, Schedule schedule) {
        return Ticketing.builder()
                .password(ticketReserveDTO.getPassword())
                .phoneNo(ticketReserveDTO.getPhoneNo())
                .customer(customer)
                .schedule(schedule)
                .build();
    }
}
