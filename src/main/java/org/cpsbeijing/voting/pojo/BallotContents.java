package org.cpsbeijing.voting.pojo;

import lombok.Data;

@Data
public class BallotContents {
    private Integer ballotId;
    private Integer serialNo;
    private Integer supervisorCount = 0;
    private Integer directorCount = 0;
    private Integer supervisorExceeded = 0;
    private Integer directorExceed = 0;
    private Iterable<BallotItem> supervisorList;
    private Iterable<BallotItem> directorList;
    private Iterable<BallotItem> extraList;
    private String createdAt;
}
