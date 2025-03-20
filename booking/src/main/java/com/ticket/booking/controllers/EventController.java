package com.ticket.booking.controllers;

import com.ticket.booking.models.Event;
import com.ticket.booking.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/event")
public class EventController {
    @Autowired private EventService eventService;

    @PostMapping("/create")
    public Event createEvent(@RequestBody Event event){
        return eventService.create(event);
    }

    @GetMapping("/all")
    public List<Event> viewEvents(){
        return eventService.read();
    }
}
