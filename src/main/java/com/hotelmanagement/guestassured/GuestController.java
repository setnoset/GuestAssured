package com.hotelmanagement.guestassured;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.beans.FeatureDescriptor;
import java.util.List;
import java.util.stream.Collectors;
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
    GuestDTO newGuest(@RequestBody Guest newGuest) {
        try {
            return GuestDTO.fromGuest(guestRepository.save(newGuest), checkInRepository);
        } catch (DataAccessException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/guests/{id}")
    @ResponseStatus(HttpStatus.OK)
    GuestDTO updateGuest(@RequestBody Guest newGuest, @PathVariable Long id) {
        return guestRepository.findById(id).map(guest -> {
            copyProperties(newGuest, guest, getNullPropertyNames(newGuest));
            return GuestDTO.fromGuest(guest, checkInRepository);
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
    GuestDTO getGuest(@PathVariable Long id) {
        Guest guest = guestRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return GuestDTO.fromGuest(guest, checkInRepository);
    }

    @GetMapping("/guests")
    @ResponseStatus(HttpStatus.OK)
    List<GuestDTO> searchGuests(@RequestParam(required = false, defaultValue = "any") String inhotel,
                             @RequestParam(required = false, defaultValue = "0") String pageid) {
        try {
            return findAll(inhotel, Integer.parseInt(pageid)).stream()
                    .map(guest -> GuestDTO.fromGuest(guest, checkInRepository))
                    .collect(Collectors.toList());
        } catch (DataAccessException | java.lang.NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    private List<Guest> findAll(String inhotel, int pageid) {
        switch (inhotel) {
            case "yes":
                return guestRepository.findAllInHotel(pageid);
            case "no":
                return guestRepository.findAllNotInHotel(pageid);
            default:
                return guestRepository.findAll(pageid);
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

    @PostMapping("/checkin")
    @ResponseStatus(HttpStatus.CREATED)
    CheckIn checkInGuestByLookup(@RequestBody CheckInDTO checkInDTO) {
        Guest guest = lookup(checkInDTO.getGuestIdentifierType(), checkInDTO.getGuestIdentifier());
        if (guest == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        CheckIn newCheckIn = new CheckIn(null, guest.getId(), checkInDTO.getDate_in(), checkInDTO.getDate_out(), checkInDTO.getParking());
        try {
            return checkInRepository.save(newCheckIn);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    private Guest lookup(String identifierType, String lookupStr) {
        switch (identifierType) {
            case "name":
                return guestRepository.findByName(lookupStr);
            case "document":
                return guestRepository.findByDocument(lookupStr);
            case "phone":
                return guestRepository.findByPhone(lookupStr);
            default:
                return null;
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
