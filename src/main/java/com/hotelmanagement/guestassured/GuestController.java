package com.hotelmanagement.guestassured;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.beans.FeatureDescriptor;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Stream;

import static org.springframework.beans.BeanUtils.copyProperties;

@RestController
public class GuestController {
    private final GuestRepository guestRepository;
    private final CheckInRepository checkInRepository;

    public GuestController(GuestRepository guestRepository, CheckInRepository checkInRepository) {
        this.guestRepository = guestRepository;
        this.checkInRepository = checkInRepository;
    }

    @PostMapping("/guests")
    @ResponseStatus(HttpStatus.CREATED)
    Guest newGuest(@RequestBody Guest newGuest) {
        try {
            return guestRepository.save(newGuest);
        } catch (DataAccessException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/guests/{id}")
    @ResponseStatus(HttpStatus.OK)
    Guest updateGuest(@RequestBody Guest newGuest, @PathVariable Long id) {
        return guestRepository.findById(id).map(guest -> {
            copyProperties(newGuest, guest, getNullPropertyNames(newGuest));
            return guestRepository.save(guest);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/guests/{id}")
    @ResponseStatus(HttpStatus.OK)
    void deleteGuest(@PathVariable Long id) {
        guestRepository.findById(id).map(guest -> {
            guestRepository.delete(guest);
            return guest;
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/guests/{id}")
    @ResponseStatus(HttpStatus.OK)
    Guest getGuest(@PathVariable Long id) {
        return guestRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/guests")
    @ResponseStatus(HttpStatus.OK)
    List<Guest> searchGuests(@RequestParam(required = false, defaultValue = "any") String inhotel,
                             @RequestParam(required = false, defaultValue = "0") String pageid) {
        try {
            switch (inhotel) {
                case "yes":
                    return guestRepository.findAllInHotel(Integer.parseInt(pageid));
                case "no":
                    return guestRepository.findAllNotInHotel(Integer.parseInt(pageid));
                default:
                    return guestRepository.findAll(Integer.parseInt(pageid));
            }
        } catch (DataAccessException | java.lang.NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/guests/{id}")
    @ResponseStatus(HttpStatus.OK)
    CheckIn checkInGuest(@RequestBody CheckIn newCheckIn, @PathVariable(name = "id") Long guestId) {
        newCheckIn.setGuest(guestId);
        try {
            return checkInRepository.save(newCheckIn);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    private static String[] getNullPropertyNames(Object source) {
        final BeanWrapper wrappedSource = new BeanWrapperImpl(source);
        return Stream.of(wrappedSource.getPropertyDescriptors())
                .map(FeatureDescriptor::getName)
                .filter(propertyName -> wrappedSource.getPropertyValue(propertyName) == null)
                .toArray(String[]::new);
    }
}
