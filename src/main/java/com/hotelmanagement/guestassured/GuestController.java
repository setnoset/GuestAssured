package com.hotelmanagement.guestassured;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.beans.FeatureDescriptor;
import java.util.stream.Stream;

import static org.springframework.beans.BeanUtils.copyProperties;

@RestController
public class GuestController {
    private final GuestRepository repository;

    public GuestController(GuestRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/guests")
    @ResponseStatus(HttpStatus.CREATED)
    Guest newGuest(@RequestBody Guest newGuest) {
        try {
            return repository.save(newGuest);
        } catch (DataAccessException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/guests/{id}")
    @ResponseStatus(HttpStatus.OK)
    Guest updateGuest(@RequestBody Guest newGuest, @PathVariable Long id) {
        return repository.findById(id).map(guest -> {
            copyProperties(newGuest, guest, getNullPropertyNames(newGuest));
            return repository.save(guest);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/guests/{id}")
    @ResponseStatus(HttpStatus.OK)
    void deleteGuest(@PathVariable Long id) {
        repository.findById(id).map(guest -> {
            repository.delete(guest);
            return guest;
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private static String[] getNullPropertyNames(Object source) {
        final BeanWrapper wrappedSource = new BeanWrapperImpl(source);
        return Stream.of(wrappedSource.getPropertyDescriptors())
                .map(FeatureDescriptor::getName)
                .filter(propertyName -> wrappedSource.getPropertyValue(propertyName) == null)
                .toArray(String[]::new);
    }
}
