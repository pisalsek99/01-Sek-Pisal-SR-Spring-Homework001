package com.example.honework001.controller;

import com.example.honework001.model.entity.Ticket;
import com.example.honework001.ticketdto.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/tickets/")
public class TicketController {

    List<Ticket> tickets = new ArrayList<>();
    private int nextTicketId = 22;
    public TicketController(){
        tickets.add(new Ticket(1, "Sok Dara", "2025-03-15", "Siem Reap", "srp", 180.0, true, Ticket.TicketStatus.BOOKED, "t1"));
        tickets.add(new Ticket(2, "Chan Mony", "2025-03-15", "Battambang", "btt", 220.0, false, Ticket.TicketStatus.CANCELLED, "t2"));
        tickets.add(new Ticket(3, "Vann Vutha", "2025-03-15", "Kampot", "kpt", 260.0, true, Ticket.TicketStatus.COMPLETED, "t3"));
        tickets.add(new Ticket(4, "Kim Sothy", "2025-03-15", "Takeo", "tk", 310.0, false, Ticket.TicketStatus.BOOKED, "t4"));
        tickets.add(new Ticket(5, "Sina Rith", "2025-03-15", "Kandal", "kdl", 370.0, true, Ticket.TicketStatus.CANCELLED, "t5"));
        tickets.add(new Ticket(6, "Bopha Chan", "2025-03-15", "Kampong Cham", "kpch", 420.0, false, Ticket.TicketStatus.COMPLETED, "t6"));
        tickets.add(new Ticket(7, "Sokhan Dara", "2025-03-15", "Mondulkiri", "mdl", 470.0, true, Ticket.TicketStatus.BOOKED, "t7"));
        tickets.add(new Ticket(8, "Chea Vichea", "2025-03-15", "Ratanakiri", "rtn", 520.0, false, Ticket.TicketStatus.CANCELLED, "t8"));
        tickets.add(new Ticket(9, "Neary Rath", "2025-03-15", "Prey Veng", "prv", 570.0, true, Ticket.TicketStatus.COMPLETED, "t9"));
        tickets.add(new Ticket(10, "Seyla Phan", "2025-03-15", "Kampong Speu", "kpst", 620.0, true, Ticket.TicketStatus.COMPLETED, "t10"));
        tickets.add(new Ticket(11, "Malis Som", "2025-03-15", "Svay Rieng", "svr", 670.0, true, Ticket.TicketStatus.BOOKED, "t11"));
        tickets.add(new Ticket(12, "Rithy Sien", "2025-03-15", "Koh Kong", "kkg", 720.0, true, Ticket.TicketStatus.BOOKED, "t12"));
        tickets.add(new Ticket(13, "Saray Neang", "2025-03-15", "Banteay Meanchey", "btm", 770.0, true, Ticket.TicketStatus.BOOKED, "t13"));
        tickets.add(new Ticket(14, "Kosal Heng", "2025-03-15", "Pailin", "pln", 820.0, true, Ticket.TicketStatus.COMPLETED, "t14"));
        tickets.add(new Ticket(15, "Ravy Kim", "2025-03-15", "Kampong Thom", "kpth", 870.0, true, Ticket.TicketStatus.BOOKED, "t15"));
    }





    @PostMapping("/bulk")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createBulkTickets(@RequestBody List<CreateTicketRequest> ticketCreateRequests) {
        if (ticketCreateRequests.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseTicket.createErrorResponse("Request list cannot be empty", HttpStatus.BAD_REQUEST));
        }

        List<TicketResponse> createdTickets = new ArrayList<>();

        for (CreateTicketRequest request : ticketCreateRequests) {
            Ticket newTicket = buildTicketFromRequest(request);
            tickets.add(newTicket);
            createdTickets.add(buildTicketResponse(newTicket));
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseTicket.createSuccessResponse("Bulk tickets created successfully", createdTickets));
    }

    @GetMapping
    public ResponseEntity<ResponseTicket<ListResponse>> getAllTickets(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        int startIndex = page * size;
        int endIndex = Math.min(startIndex + size, tickets.size());

        List<TicketResponse> ticketResponses = tickets.subList(startIndex, endIndex)
                .stream()
                .map(ticket -> new TicketResponse(
                        ticket.getTicketId(),
                        ticket.getPassengerName(),
                        ticket.getTravelDate(),
                        ticket.getSourceStation(),
                        ticket.getDestinationStation(),
                        ticket.getPrice(),
                        ticket.getPaymentStatus(),
                        ticket.getTicketStatus(),
                        ticket.getSeatNumber()
                ))
                .collect(Collectors.toList());
        int totalElements = tickets.size();
        int totalPages = (int) Math.ceil((double) totalElements / size);

        Pagination paginationInfo = new Pagination(totalElements, page + 1, size, totalPages);
        ListResponse responsePayload = new ListResponse(ticketResponses, paginationInfo);

        return ResponseEntity.ok(ResponseTicket.createSuccessResponse("Tickets retrieved successfully", responsePayload));
    }



    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createNewTicket(@RequestBody CreateTicketRequest ticketCreateRequest) {
        Ticket newTicket = buildTicketFromRequest(ticketCreateRequest);
        tickets.add(newTicket);

        TicketResponse ticketResponse = buildTicketResponse(newTicket);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseTicket.createSuccessResponse("Ticket created successfully", ticketResponse));
    }

    private Ticket buildTicketFromRequest(CreateTicketRequest ticketCreateRequest) {
        return new Ticket(
                nextTicketId++,
                ticketCreateRequest.passengerName(),
                ticketCreateRequest.travelDate(),
                ticketCreateRequest.sourceStation(),
                ticketCreateRequest.destinationStation(),
                ticketCreateRequest.price(),
                ticketCreateRequest.paymentStatus(),
                ticketCreateRequest.ticketStatus(),
                ticketCreateRequest.seatNumber()
        );
    }
    private TicketResponse buildTicketResponse(Ticket ticket) {
        return new TicketResponse(
                ticket.getTicketId(),
                ticket.getPassengerName(),
                ticket.getTravelDate(),
                ticket.getSourceStation(),
                ticket.getDestinationStation(),
                ticket.getPrice(),
                ticket.getPaymentStatus(),
                ticket.getTicketStatus(),
                ticket.getSeatNumber()
        );
    }



    @PutMapping("/{id}")
    public ResponseEntity<?> updateTicket(@PathVariable Integer id, @RequestBody UpdateRequestTicket updateRequest) {
        Optional<Ticket> ticketOptional = tickets.stream()
                .filter(ticket -> ticket.getTicketId().equals(id))
                .findFirst();

        if (ticketOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseTicket.createErrorResponse("Ticket not found", HttpStatus.NOT_FOUND));
        }

        final Ticket ticket = getTicket(updateRequest, ticketOptional);

        TicketResponse ticketResponse = new TicketResponse(
                ticket.getTicketId(),
                ticket.getPassengerName(),
                ticket.getTravelDate(),
                ticket.getSourceStation(),
                ticket.getDestinationStation(),
                ticket.getPrice(),
                ticket.getPaymentStatus(),
                ticket.getTicketStatus(),
                ticket.getSeatNumber()
        );
        return ResponseEntity.ok(ResponseTicket.createSuccessResponse("Ticket updated successfully", ticketResponse));
    }

    private static Ticket getTicket(UpdateRequestTicket updateRequest, Optional<Ticket> ticketOptional) {
        Ticket ticket = ticketOptional.get();
        ticket.setPassengerName(updateRequest.passengerName());
        ticket.setTravelDate(updateRequest.travelDate());
        ticket.setSourceStation(updateRequest.sourceStation());
        ticket.setDestinationStation(updateRequest.destinationStation());
        ticket.setPrice(updateRequest.price());
        ticket.setPaymentStatus(updateRequest.paymentStatus());
        ticket.setTicketStatus(updateRequest.ticketStatus());
        ticket.setSeatNumber(updateRequest.seatNumber());
        return ticket;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseTicket<TicketResponse>> getTicketByID(@PathVariable Integer id) {
        Optional<Ticket> ticketOptional = tickets.stream()
                .filter(ticket -> ticket.getTicketId().equals(id))
                .findFirst();

        if (ticketOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseTicket.createErrorResponse("Ticket not found", HttpStatus.NOT_FOUND));
        }

        Ticket ticket = ticketOptional.get();
        TicketResponse ticketResponse = TicketResponse.builder()
                .ticketId(ticket.getTicketId())
                .passengerName(ticket.getPassengerName())
                .travelDate(ticket.getTravelDate())
                .sourceStation(ticket.getSourceStation())
                .destinationStation(ticket.getDestinationStation())
                .price(ticket.getPrice())
                .paymentStatus(ticket.getPaymentStatus())
                .ticketStatus(ticket.getTicketStatus())
                .seatNumber(ticket.getSeatNumber())
                .build();

        return ResponseEntity.ok(ResponseTicket.createSuccessResponse("Ticket retrieved by id successfully.", ticketResponse));
    }
    @GetMapping("/search")
    public ResponseEntity<ResponseTicket<ListResponse>> searchTicketsByPassengerName(@RequestParam String passengerName,
                                                                                              @RequestParam(defaultValue = "0") int page,
                                                                                              @RequestParam(defaultValue = "10") int size) {

        List<Ticket> filteredTickets = tickets.stream()
                .filter(ticket -> ticket.getPassengerName().toLowerCase().contains(passengerName.toLowerCase()))
                .toList();

        if (filteredTickets.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseTicket.createErrorResponse("No tickets found for given passenger name", HttpStatus.NOT_FOUND));
        }

        int startIndex = page * size;
        int endIndex = Math.min(startIndex + size, filteredTickets.size());

        List<TicketResponse> ticketResponses = filteredTickets.subList(startIndex, endIndex)
                .stream()
                .map(ticket -> new TicketResponse(
                        ticket.getTicketId(),
                        ticket.getPassengerName(),
                        ticket.getTravelDate(),
                        ticket.getSourceStation(),
                        ticket.getDestinationStation(),
                        ticket.getPrice(),
                        ticket.getPaymentStatus(),
                        ticket.getTicketStatus(),
                        ticket.getSeatNumber()
                ))
                .collect(Collectors.toList());



        int totalElements = filteredTickets.size();
        int totalPages = (int) Math.ceil((double) totalElements / size);

        Pagination paginationInfo = new Pagination(totalElements, page + 1, size, totalPages);
        ListResponse responsePayload = new ListResponse(ticketResponses, paginationInfo);

        return ResponseEntity.ok(ResponseTicket.createSuccessResponse("Tickets retrieved successfully", responsePayload));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTicket(@PathVariable Integer id) {
        Optional<Ticket> ticketOptional = tickets.stream()
                .filter(ticket -> ticket.getTicketId().equals(id))
                .findFirst();

        if (ticketOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseTicket.createErrorResponse("Ticket not found", HttpStatus.NOT_FOUND));
        }

        tickets.remove(ticketOptional.get());

        return ResponseEntity.ok(ResponseTicket.createSuccessResponse("Ticket deleted successfully", null));
    }

    @GetMapping("/filter")
    public ResponseEntity<ResponseTicket<ListResponse>> filterTicketsByStatusAndDate(
            @RequestParam Ticket.TicketStatus ticketStatus,
            @RequestParam String travelDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        // Parse travel date to ensure it's in a valid format
        Date parsedDate;
        try {
            parsedDate = Date.from(Instant.parse(travelDate));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseTicket.createErrorResponse("Invalid travel date format. Expected format: yyyy-MM-dd'T'HH:mm:ss'Z'", HttpStatus.BAD_REQUEST));
        }

        // Filter tickets based on ticketStatus and travelDate
        List<Ticket> filteredTickets = tickets.stream()
                .filter(ticket -> ticket.getTicketStatus().equals(ticketStatus))
                .filter(ticket -> ticket.getTravelDate().equals(travelDate))
                .collect(Collectors.toList());

        if (filteredTickets.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseTicket.createErrorResponse("No tickets found for the given status and travel date", HttpStatus.NOT_FOUND));
        }

        int startIndex = page * size;
        int endIndex = Math.min(startIndex + size, filteredTickets.size());

        List<TicketResponse> ticketResponses = filteredTickets.subList(startIndex, endIndex)
                .stream()
                .map(ticket -> new TicketResponse(
                        ticket.getTicketId(),
                        ticket.getPassengerName(),
                        ticket.getTravelDate(),
                        ticket.getSourceStation(),
                        ticket.getDestinationStation(),
                        ticket.getPrice(),
                        ticket.getPaymentStatus(),
                        ticket.getTicketStatus(),
                        ticket.getSeatNumber()
                ))
                .collect(Collectors.toList());

        int totalElements = filteredTickets.size();
        int totalPages = (int) Math.ceil((double) totalElements / size);

        Pagination paginationInfo = new Pagination(totalElements, page + 1, size, totalPages);
        ListResponse responsePayload = new ListResponse(ticketResponses, paginationInfo);

        return ResponseEntity.ok(ResponseTicket.createSuccessResponse("Filtered tickets retrieved successfully", responsePayload));
    }


}