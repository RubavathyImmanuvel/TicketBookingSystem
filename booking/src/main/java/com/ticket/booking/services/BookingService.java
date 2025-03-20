package com.ticket.booking.services;

import com.ticket.booking.models.Booking;
import com.ticket.booking.models.Event;
import com.ticket.booking.models.User;
import com.ticket.booking.repositories.BookingRepository;
import com.ticket.booking.repositories.EventRepository;
import com.ticket.booking.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class BookingService {
    @Autowired private BookingRepository bookingRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private EventRepository eventRepository;

    @Async
    @Transactional
    public CompletableFuture<Booking> createInAsync(SecurityContext securityContext, Long eventId, int seats){
        return CompletableFuture.supplyAsync(() -> {
            SecurityContextHolder.setContext(securityContext);;
            return create(eventId, seats);
        });
    }

    public synchronized Booking create(Long eventId, int seats){
        Event event = eventRepository.findById(eventId).orElseThrow();

        if(event.getSeatsAvailable() >= seats){
            event.setSeatsAvailable(event.getSeatsAvailable() - seats);
            eventRepository.save(event);
       }
        else{
            return null;
        }

        String userEmail = getLoggedInUserEmail();
        User user = userRepository.findByEmail(userEmail).orElseThrow();

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setEvent(event);
        booking.setSeatsBooked(seats);
        booking.setStatus("Confirmed");
        return bookingRepository.save(booking);
    }

    private String getLoggedInUserEmail(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails){
            return ((UserDetails) principal).getUsername();
        }
        return principal.toString();
    }
}
