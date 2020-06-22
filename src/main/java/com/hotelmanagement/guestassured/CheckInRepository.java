package com.hotelmanagement.guestassured;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface CheckInRepository extends PagingAndSortingRepository<CheckIn, Long> {
}
