package com.hotelmanagement.guestassured;

import java.time.LocalDateTime;

public class CheckInDTO {
    private String guestIdentifierType;
    private String guestIdentifier;
    private LocalDateTime date_in;
    private LocalDateTime date_out;
    private Boolean parking;

    public String getGuestIdentifierType() {
        return guestIdentifierType;
    }

    public String getGuestIdentifier() {
        return guestIdentifier;
    }

    public LocalDateTime getDate_in() {
        return date_in;
    }

    public LocalDateTime getDate_out() {
        return date_out;
    }

    public Boolean getParking() {
        return parking;
    }
}
