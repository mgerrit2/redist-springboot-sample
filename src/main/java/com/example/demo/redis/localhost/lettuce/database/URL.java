package com.example.demo.redis.localhost.lettuce.database;

import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "url")
public class URL {

    @Id
    private String id;

    private String url;

    @Column(name = "time_processed")
    private Integer timeProcessed;

    @Column(name = "contect_type")
    private String contectType;

    @Column(name = "last_proccssed")
    private LocalDateTime lastProccssed;

    @Column(name = "date_created")
    private LocalDateTime dateCreated;

}
