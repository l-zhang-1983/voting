package org.cpsbeijing.voting.repository;

import org.cpsbeijing.voting.entity.BallotInfo;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BallotInfoRepository extends PagingAndSortingRepository<BallotInfo, Integer> {
    BallotInfo findBySerialNo(Integer serialNo);

    BallotInfo findByBallotId(Integer ballotId);
}
