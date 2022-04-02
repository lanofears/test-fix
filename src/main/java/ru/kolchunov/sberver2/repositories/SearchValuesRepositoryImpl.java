package ru.kolchunov.sberver2.repositories;

import org.springframework.stereotype.Repository;
import ru.kolchunov.sberver2.models.Values;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.stream.Stream;

@Repository
public class SearchValuesRepositoryImpl implements SearchValuesRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Stream<Values> searchByRowId(Long dictionaryId, Long rowId) {
        return entityManager.createQuery("select v from Values v " +
                "inner join RowValues r on r.id = v.idRow " +
                "where v.idRow = :rowId and r.idDict = :dictionaryId", Values.class)
            .setParameter("rowId", rowId)
            .setParameter("dictionaryId", dictionaryId)
            .getResultStream();
    }
}
