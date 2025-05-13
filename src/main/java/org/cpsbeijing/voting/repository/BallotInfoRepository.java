package org.cpsbeijing.voting.repository;

import org.cpsbeijing.voting.entity.BallotInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BallotInfoRepository extends CrudRepository<BallotInfo, Integer> {
    BallotInfo findBySerialNo(Integer serialNo);

    BallotInfo findByBallotId(Integer ballotId);
}
