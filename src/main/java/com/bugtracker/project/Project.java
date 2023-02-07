package com.bugtracker.project;

import com.bugtracker.enums.Priority;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
public class Project {

    @Id
    @GeneratedValue
    Long id;

    @Column(nullable = false, unique = true)
    String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    Priority priority = Priority.NORMAL;


    @NotEmpty
    @Size(min = 5, max = 255)
    @Column(nullable = false)
    String content;

    @Column(nullable = false)
    @ColumnDefault(value = "true")
    Boolean enabled = true;
}
