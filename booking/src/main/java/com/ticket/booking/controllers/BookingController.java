package com.ticket.booking.controllers;

import com.ticket.booking.models.Booking;
import com.ticket.booking.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/booking")
public class BookingController {
    @Autowired private BookingService bookingService;

    @PostMapping("/create")
    public CompletableFuture<Booking> create(SecurityContext securityContext, @RequestParam Long eventId, @RequestParam int seats){
        return bookingService.createInAsync(securityContext, eventId, seats);
    }
}
