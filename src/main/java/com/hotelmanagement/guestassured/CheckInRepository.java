package com.hotelmanagement.guestassured;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CheckInRepository extends CrudRepository<CheckIn, Long> {
    long countByGuest(Long guestid);

    CheckIn findFirstByGuestOrderByDateOut(Long guestid);

    List<CheckIn> findAllByGuestOrderByDateOut(Long guestid);

    @Query(value = "SELECT SUM(price) FROM check_in WHERE guest = :guest")
    Optional<Float> guestExpenditure(Long guest);
}
