package org.cpsbeijing.voting.pojo;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.List;

@Data
public class BallotContents {
    private Integer ballotId;
    private Integer serialNo;
    private Integer supervisorCount = 0;
    private Integer directorCount = 0;
    private Integer supervisorExceeded = 0;
    private Integer directorExceed = 0;
    private Iterable<BallotItem> ballotItemList;
    private String createdAt;
}
