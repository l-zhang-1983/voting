package org.cpsbeijing.voting.repository;

import org.cpsbeijing.voting.entity.BallotDetails;
import org.cpsbeijing.voting.entity.BallotInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BallotDetailsRepository extends CrudRepository<BallotDetails, Integer> {
    void removeBallotDetailsByBallot(BallotInfo ballot);
}
