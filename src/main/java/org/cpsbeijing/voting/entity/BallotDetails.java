package org.cpsbeijing.voting.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ballot_details")
@Getter
@Setter
public class BallotDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detail_id", nullable = false)
    private Integer detailId;

    @ManyToOne
    @JoinColumn(name = "ballot_id", referencedColumnName = "ballot_id", nullable = false)
    private BallotInfo ballot;

    @ManyToOne
    @JoinColumn(name = "candidate_id", referencedColumnName = "candidate_id", nullable = false)
    private ConfigCandidateInfo candidate;

    @Column(name = "candidate_type", nullable = false)
    private Integer candidateType;

    @Column(name = "created_at", nullable = false, length = 32)
    private String createdAt = "20250101000000000";

    @Column(name = "updated_at", nullable = false, length = 32)
    private String updatedAt = "20250101000000000";

}