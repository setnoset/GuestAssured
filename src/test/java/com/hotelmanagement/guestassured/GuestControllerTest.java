package com.hotelmanagement.guestassured;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GuestControllerTest {
    @Mock
    GuestRepository guestRepositoryMock;

    @Mock
    CheckInRepository checkInRepositoryMock;

    @InjectMocks
    GuestController guestController;

    @Test
    public void testSuccessfulGuestRegistration() {
        Guest guest = new Guest(null, "Fulano Teste 2", "297884743", "5541948226148");

        when(guestRepositoryMock.save(guest)).thenReturn(new Guest(7L, guest.getName(), guest.getDocument(), guest.getPhone()));
        when(checkInRepositoryMock.countByGuest(7L)).thenReturn(0L);
        when(checkInRepositoryMock.findFirstByGuestOrderByDateOut(7L)).thenReturn(null);
        when(checkInRepositoryMock.guestExpenditure(7L)).thenReturn(Optional.empty());

        GuestDTO guestDTO = guestController.newGuest(guest);

        assertEquals(7L, guestDTO.getId());
        assertEquals(guest.getName(), guestDTO.getName());
        assertEquals(guest.getDocument(), guestDTO.getDocument());
        assertEquals(guest.getPhone(), guestDTO.getPhone());
        assertEquals(0L, guestDTO.getCheckInCount());
        assertNull(guestDTO.getLastCheckInCost());
        assertEquals(0.0f, guestDTO.getTotalSpent());
    }

    @Test
    public void testGuestRegistrationWithMissingDocument() {
        Guest guest = new Guest(null, "Fulano Teste 2", null, "5541948226148");

        when(guestRepositoryMock.save(guest)).thenThrow(new DataIntegrityViolationException("Attempting to insert null value into non null column"));

        assertThrows(ResponseStatusException.class, () -> guestController.newGuest(guest));
    }
}
