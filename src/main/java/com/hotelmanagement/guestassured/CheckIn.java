package com.hotelmanagement.guestassured;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class CheckIn {
    private static final float WEEKDAY_ROOM_PRICE = 150.00f;
    private static final float WEEKEND_ROOM_PRICE = 210.00f;
    private static final float WEEKDAY_PARKING_PRICE = 15.00f;
    private static final float WEEKEND_PARKING_PRICE = 20.00f;

    private final @JsonProperty @Id Long id;
    private @JsonProperty Long guest;
    @Column("date_in")
    private final @JsonProperty LocalDateTime dateIn;
    @Column("date_out")
    private final @JsonProperty LocalDateTime dateOut;
    private @JsonProperty Boolean parking;
    private @JsonProperty Float price;

    public CheckIn(Long id, Long guest, LocalDateTime dateIn, LocalDateTime dateOut, Boolean parking) {
        this.id = id;
        this.guest = guest;
        this.dateIn = dateIn;
        this.dateOut = dateOut;
        this.parking = parking;
        this.price = calculatePrice();
    }

    public void setGuest(Long guestId) {
        this.guest = guestId;
    }

    public Long getGuest() {
        return guest;
    }

    private Float calculatePrice() {
        LocalDate start = LocalDate.from(dateIn);
        LocalDate end = LocalDate.from(dateOut);
        long days = start.until(end).getDays();

        long workingDayCount = 5 * (days / 7);
        for (DayOfWeek dof = start.getDayOfWeek(); !dof.equals(end.getDayOfWeek()); dof = dof.plus(1))
            if (!isWeekend(dof))
                workingDayCount += 1;
        long weekendDayCount = days - workingDayCount;

        return workingDayCount * weekday_price() + weekendDayCount * weekend_price();
    }

    private static boolean isWeekend(DayOfWeek dof) {
        return dof.equals(DayOfWeek.SATURDAY) || dof.equals(DayOfWeek.SUNDAY);
    }

    private float weekday_price() {
        return WEEKDAY_ROOM_PRICE + (parking ? WEEKDAY_PARKING_PRICE : 0);
    }

    private float weekend_price() {
        return WEEKEND_ROOM_PRICE + (parking ? WEEKEND_PARKING_PRICE : 0);
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
}
