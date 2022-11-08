package com.softserve.itacademy.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "user_tokens")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class RegisteredUser {
    @Id
    @Column(name = "user_id")
    private Long userId;

    @OneToOne
    @PrimaryKeyJoinColumn(name="user_id", referencedColumnName="id")
    private User user;

    @Column(name = "token", nullable = false)
    private String token;

}
