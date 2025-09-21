package com.mechtrack.model.entity;

import com.mechtrack.model.enums.JobStatus;
import com.mechtrack.model.enums.JobType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "job")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "car_model")
    private String carModel;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "date")
    private LocalDate date;

    @Column(precision = 10, scale = 2)
    private BigDecimal income;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobStatus status = JobStatus.WAITING;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobType type = JobType.GENERAL_MAINTENANCE;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Part> parts;
} 