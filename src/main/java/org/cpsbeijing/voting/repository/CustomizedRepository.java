package org.cpsbeijing.voting.repository;

import java.util.List;

public interface CustomizedRepository {
    List<Object[]> getBallotItems(Integer ballotId);
    List<Object[]> getBallotItems();
}
