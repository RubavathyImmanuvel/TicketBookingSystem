package com.ticket.booking.services;

import com.ticket.booking.models.Event;
import com.ticket.booking.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {
    @Autowired private EventRepository eventRepository;

    public Event create(Event event){
        return eventRepository.save(event);
    }

    public List<Event> read(){
        return eventRepository.findAll();
    }
}
