package com.hotelmanagement.guestassured;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("guests")
public class Guest {
    private final @JsonProperty @Id Long id;
    private @JsonProperty String name;
    private @JsonProperty String document;
    private @JsonProperty String phone;

    public Guest(Long id, String name, String document, String phone) {
        this.id = id;
        this.name = name;
        this.document = document;
        this.phone = phone;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
