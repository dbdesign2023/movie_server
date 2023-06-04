package dbclass.movie.mapper;

import dbclass.movie.domain.schedule.Schedule;
import dbclass.movie.domain.ticket.Ticket;
import dbclass.movie.domain.user.Customer;
import dbclass.movie.dto.ticket.CustomerTicketDTO;
import dbclass.movie.dto.ticket.TicketDetailCustomerDTO;
import dbclass.movie.dto.ticket.TicketReserveDTO;
import dbclass.movie.dto.ticket.TicketShortDTO;

import java.sql.Timestamp;

public class TicketMapper {

    private TicketMapper() {}

    public static Ticket TicketReserveDTOToTicket(TicketReserveDTO ticketReserveDTO, Customer customer, Schedule schedule) {
        return Ticket.builder()
                .password(ticketReserveDTO.getPassword())
                .phoneNo(ticketReserveDTO.getPhoneNo())
                .customer(customer)
                .schedule(schedule)
                .build();
    }

    public static TicketShortDTO ticketToTicketShortDTO(Ticket ticket, String movieId, String theaterName, Timestamp startTime) {
        return TicketShortDTO.builder()
                .ticketId(ticket.getTicketId())
                .ticketingTime(ticket.getTicketTime())
                .movieName(movieId)
                .theaterName(theaterName)
                .startTime(startTime)
                .build();
    }

    public static CustomerTicketDTO ticketToTicketDTO(Ticket ticket) {
        return CustomerTicketDTO.builder()
                .ticketId(ticket.getTicketId())
                .ticketTime(ticket.getTicketTime())
                .loginId(ticket.getCustomer().getLoginId())
                .scheduleId(ticket.getSchedule().getScheduleId())
                .build();
    }

    /**
     * 좌석은 제외
     **/
    public static TicketDetailCustomerDTO ticketToTicketDetailCustomerDTO(Ticket ticket) {
        return TicketDetailCustomerDTO.builder()
                .ticketId(ticket.getTicketId())
                .ticketTime(ticket.getTicketTime())
                .movieId(ticket.getSchedule().getMovie().getMovieId())
                .movieTitle(ticket.getSchedule().getMovie().getTitle())
                .theaterName(ticket.getSchedule().getTheater().getName())
                .floor(ticket.getSchedule().getTheater().getFloor())
                .startTime(ticket.getSchedule().getStartTime())
                .runningTime(ticket.getSchedule().getMovie().getRunningTime())
                .posterFileName(ticket.getSchedule().getMovie().getPoster().getFileName())
                .build();
    }
}
