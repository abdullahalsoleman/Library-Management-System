package com.library.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@Entity
public class BorrowingRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long bookId;

    @Column(nullable = false)
    private Long patronId;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date borrowDate;

    @Temporal(TemporalType.DATE)
    private Date returnDate;
}
