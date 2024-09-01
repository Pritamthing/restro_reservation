package com.assesment.persistent.model;

import com.assesment.web.enums.TableStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tables")
public class PersistentTableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private int capacity;

    @ManyToOne
    @JoinColumn(name = "created_by_id")
    private PersistentUserEntity createdBy;

    @Enumerated(EnumType.STRING)
    private TableStatus status = TableStatus.AVAILABLE;
}