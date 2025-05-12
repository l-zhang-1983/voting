package org.cpsbeijing.voting.repository;

import org.cpsbeijing.voting.pojo.BallotItem;
import org.hibernate.SQLQuery;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;

@Repository
public class CustomizedRepositoryImpl implements CustomizedRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<BallotItem> getBallotItems(Integer ballotId) {
        String sql = "select cci.candidate_id, cci.candidate_type, cci.province_code, cci.candidate_name, " +
                "case when bd.candidate_id is not null then 1 else 0 end checked " +
                "from config_candidate_info cci " +
                "left join ballot_details bd " +
                "on cci.candidate_id = bd.candidate_id " +
                "and bd.ballot_id = :ballotId " +
                "order by cci.candidate_id, cci.candidate_type";
        Query namedQuery = entityManager.createNamedQuery(sql, BallotItem.class);
        namedQuery.setParameter("ballotId", ballotId);
        return namedQuery.getResultList();
    }

    @Override
    public List<Object[]> getBallotItems() {
        String sql = "select cci.candidate_id, cci.candidate_type, cci.province_code, cci.candidate_name, " +
                "0 as checked from config_candidate_info cci " +
                "order by cci.candidate_id, cci.candidate_type";
        Query namedQuery = entityManager.createNativeQuery(sql);
        return namedQuery.getResultList();
    }
}
