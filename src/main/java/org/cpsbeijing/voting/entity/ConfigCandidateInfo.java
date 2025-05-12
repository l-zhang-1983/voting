package org.cpsbeijing.voting.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "config_candidate_info")
@Getter
@Setter
public class ConfigCandidateInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "candidate_id", nullable = false)
    private Integer candidateId;

    @ManyToOne
    @JoinColumn(name = "province_code", referencedColumnName = "province_code", nullable = false)
    private ConfigProvince province;

    @Column(name = "candidate_name", nullable = false, length = 16)
    private String candidateName;

    @Column(name = "candidate_type", nullable = false)
    private Integer candidateType;

    @Column(name = "is_previous_staff", nullable = false)
    private Integer isPreviousStaff;

}