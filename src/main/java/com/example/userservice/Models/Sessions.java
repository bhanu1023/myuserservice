package com.example.userservice.Models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;


@Entity(name = "sessions")
@Getter
@Setter
public class Sessions extends BaseModel{
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
