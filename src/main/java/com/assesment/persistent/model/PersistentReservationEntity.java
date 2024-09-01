package com.assesment.persistent.model;

import com.assesment.web.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reservation")
public class PersistentReservationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "table_id")
    private PersistentTableEntity table;

    @Column(nullable = false)
    private LocalDateTime reservationTime;

    @Column(nullable = true)
    private LocalDateTime endTime;

    @Column(nullable = false)
    private int numberOfCustomers;

    @ManyToOne
    @JoinColumn(name = "reservation_by_id")
    private PersistentUserEntity reservationBy;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status = ReservationStatus.CONFIRMED;

}