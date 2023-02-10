package com.bugtracker.auth;

import com.bugtracker.enums.AuthorityName;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Authority {

    public static final String ROLE_PREFIX = "ROLE_";

    @Id
    @GeneratedValue
    Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    AuthorityName authority;

    public Authority(AuthorityName authority) {
        this.authority = authority;
    }
}