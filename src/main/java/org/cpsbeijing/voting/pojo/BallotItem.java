package org.cpsbeijing.voting.pojo;

import lombok.Data;

@Data
public class BallotItem {
    private Integer candidateId;
    private String candidateName;
    private Integer candidateType;
    private Integer provinceCode;
    private String provinceName;
    private Integer checked;
}
