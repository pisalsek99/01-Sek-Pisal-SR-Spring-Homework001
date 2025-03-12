package com.example.honework001.ticketdto;

import lombok.Builder;
import com.example.honework001.model.entity.Ticket;

@Builder
public record TicketResponse(
        int ticketId,
        String passengerName,
        String travelDate,
        String sourceStation,
        String destinationStation,
        Double price,
        Boolean paymentStatus,
        Ticket.TicketStatus ticketStatus,
        String seatNumber
) {
}