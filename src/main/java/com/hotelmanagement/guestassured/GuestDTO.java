package com.hotelmanagement.guestassured;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;

public class GuestDTO {
    private final @JsonProperty @Id Long id;
    private @JsonProperty String name;
    private @JsonProperty String document;
    private @JsonProperty String phone;
    private @JsonProperty Long checkInCount;
    private @JsonProperty Float lastCheckInCost;
    private @JsonProperty Float totalSpent;

    private GuestDTO(Long id, String name, String document, String phone, Long checkInCount, Float lastCheckInCost, Float totalSpent) {
        this.id = id;
        this.name = name;
        this.document = document;
        this.phone = phone;
        this.checkInCount = checkInCount;
        this.lastCheckInCost = lastCheckInCost;
        this.totalSpent = totalSpent;
    }

    static GuestDTO fromGuest(Guest guest, CheckInRepository repository) {
        System.out.println(guest.getId());
        return new GuestDTO(
                guest.getId(), guest.getName(), guest.getDocument(), guest.getPhone(),
                repository.countByGuest(guest.getId()),
                getLastCheckInCost(guest.getId(), repository),
                repository.guestExpenditure(guest.getId()).orElse(0.0f)
        );
    }

    private static Float getLastCheckInCost(Long guestid, CheckInRepository repository) {
        CheckIn checkIn = repository.findFirstByGuestOrderByDateOut(guestid);
        return checkIn != null ? checkIn.getPrice() : null;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDocument() {
        return document;
    }

    public String getPhone() {
        return phone;
    }

    public Long getCheckInCount() {
        return checkInCount;
    }

    public Float getLastCheckInCost() {
        return lastCheckInCost;
    }

    public Float getTotalSpent() {
        return totalSpent;
    }
}
