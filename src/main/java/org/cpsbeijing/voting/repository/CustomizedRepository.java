package org.cpsbeijing.voting.repository;

import org.cpsbeijing.voting.pojo.BallotContents;
import org.cpsbeijing.voting.pojo.BallotItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

public interface CustomizedRepository {
    List<BallotItem> getBallotItems(Integer ballotId);
    List<Object[]> getBallotItems();
}
