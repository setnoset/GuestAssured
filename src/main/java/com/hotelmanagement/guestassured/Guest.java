package com.hotelmanagement.guestassured;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("guests")
public class Guest {
    private final @Id @JsonProperty Long id;
    private @JsonProperty String name;
    private @JsonProperty String document;
    private @JsonProperty String phone;

    public Guest(Long id, String name, String document, String phone) {
        this.id = id;
        this.name = name;
        this.document = document;
        this.phone = phone;
    }
}
