package com.hotelmanagement.guestassured;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface GuestRepository extends PagingAndSortingRepository<Guest, Long> {

    List<Guest> findAllByOrderById(Pageable pageable);

    @Query(value = "SELECT * FROM guests ORDER BY id LIMIT 10 OFFSET 10*:pageid")
    List<Guest> findAll(int pageid);

    @Query(value = "SELECT * FROM guests WHERE id IN (SELECT guest FROM checkin WHERE date_in < now() AND now() < date_out) ORDER BY id LIMIT 10 OFFSET 10*:pageid")
    List<Guest> findAllInHotel(int pageid);

    @Query(value = "SELECT * FROM guests WHERE id NOT IN (SELECT guest FROM checkin WHERE date_in < now() AND now() < date_out) ORDER BY id LIMIT 10 OFFSET 10*:pageid")
    List<Guest> findAllNotInHotel(int pageid);

}
