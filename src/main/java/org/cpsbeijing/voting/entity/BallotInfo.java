package org.cpsbeijing.voting.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ballot_info")
@Getter
@Setter
public class BallotInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ballot_id", nullable = false)
    private Integer ballotId;

    @Column(name = "serial_no", nullable = false)
    private Integer serialNo;

    @Column(name = "supervisor_count", nullable = false)
    private Integer supervisorCount = 0;

    @Column(name = "director_count", nullable = false)
    private Integer directorCount = 0;

    @Column(name = "supervisor_exceeded", nullable = false)
    private Integer supervisorExceeded = 0;

    @Column(name = "director_exceed")
    private Integer directorExceed = 0;

    @Column(name = "created_at", nullable = false, length = 32)
    private String createdAt = "20250101000000000";

    @Column(name = "updated_at", nullable = false, length = 32)
    private String updatedAt = "20250101000000000";

}