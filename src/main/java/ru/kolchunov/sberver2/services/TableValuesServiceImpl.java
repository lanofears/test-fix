package ru.kolchunov.sberver2.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import ru.kolchunov.sberver2.exceptions.NotFoundException;
import ru.kolchunov.sberver2.models.*;
import ru.kolchunov.sberver2.repositories.RowValuesRepositiry;
import ru.kolchunov.sberver2.repositories.StructureDictionaryRepository;
import ru.kolchunov.sberver2.repositories.ValuesRepository;
import ru.kolchunov.sberver2.requests.*;
import ru.kolchunov.sberver2.responses.FieldValue;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@EnableTransactionManagement
@Service
public class TableValuesServiceImpl implements TableValuesService {

    @Autowired
    ValuesRepository valuesRepository;
    @Autowired
    RowValuesRepositiry rowValuesRepositiry;
    @Autowired
    StructureDictionaryRepository structureDictionaryRepository;

    /**
     * Insert new values int the Table of values
     *
     * @param insertDictRequest {@link InsertDictRequest}
     */
    @Override
    @Transactional
    public List<FieldValue> insert(InsertDictRequest insertDictRequest) {
        log.info("INSERT VALUES {}", insertDictRequest);

        Long dictionaryId = insertDictRequest.getIdDict();
        RowValues rowValues = new RowValues();
        rowValues.setIdDict(insertDictRequest.getIdDict());
        rowValuesRepositiry.save(rowValues);

        Map<String, StructureDictionary> fields = getDictionaryFields(dictionaryId);
        Map<Long, StructureDictionary> fieldsById = new HashMap<>(fields.size());

        for (InsertDictRequest.FieldValue fieldValue : insertDictRequest.getFieldsValue()) {
            String fieldName = fieldValue.getName();
            StructureDictionary field = fields.get(fieldName);
            if (field == null) {
                throw new NotFoundException("Field with name " + fieldName + " not found");
            }
            fieldsById.put(field.getId(), field);

            Values values = new Values();
            values.setIdField(field.getId());
            values.setValue(fieldValue.getValue());
            values.setIdRow(rowValues.getId());
            valuesRepository.save(values);
        }

        return searchRow(dictionaryId, rowValues.getId(), fieldsById);
    }

    /**
     * Search rows by fields
     *
     * @param searchDictRequest {@link SearchDictRequest}
     */
    @Override
    @Transactional
    public FieldValue search(SearchDictRequest searchDictRequest) {
//        //Session session = SessionFactoryConfig.getCurrentSessionFromConfig();
//        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
//        FieldValue searchDictRes = new FieldValue();
//        Dictionary dictionary = dictionaryRepository.getById(searchDictRequest.getIdDict());
//
//        searchDictRes.setName(dictionary.getName());
//        searchDictRes.setCode(dictionary.getCode());
//
//        List<SearchDictRequest.SearchTerm> searchTerms = searchDictRequest.getSearchTerms();
//
//        Query queryIdFieldToName = session.createQuery("from StructureDictionary str where idDictionary = " + searchDictRequest.getIdDict(), StructureDictionary.class);
//
//        Map<String, Long> mapIdFieldToName = new HashMap<>();
//        try {
//            mapIdFieldToName = (Map<String, Long>) queryIdFieldToName.stream()
//                    .collect(
//                            Collectors.toMap(
//                                    x -> ((StructureDictionary) x).getName(),
//                                    x -> ((StructureDictionary) x).getId()
//                            )
//                    );
//        }
//        catch (ClassCastException e){
//            e.printStackTrace();
//            throw new IllegalArgumentException("Name and Id of the StructureDictionary not cast to fields of the Map<nameField, idField> ");
//        }
//
//        StringBuffer stringBufferQuery = new StringBuffer("select v.idRow, v.idField, str.name, v.value, str.dataType from RowValues r " +
//                "inner join Values v on r.idDict = " + searchDictRequest.getIdDict() + " and r.id = v.idRow " +
//                "inner join StructureDictionary str on str.id = v.idField");
//
//        int countJoin = 0;
//        for (SearchDictRequest.SearchTerm searchTerm : searchTerms) {
//            stringBufferQuery.append(" inner join Values v" + countJoin + " on v.idRow = v" + countJoin + ".idRow and v" + countJoin + ".idField = '" + mapIdFieldToName.get(searchTerm.getName()) + "'" +
//                    " and v" + countJoin + ".value " + conditionSearchToString(searchTerm.getCondition()) + " '" + searchTerm.getValue() + "'");
//            countJoin++;
//        }
//
//        String selectQuery = stringBufferQuery.toString();
//        Query query = session.createQuery(selectQuery);
//
//        List<FieldValue.FieldValue> resultList = query.list();
//        searchDictRes.setFieldsValue(resultList);
//        return searchDictRes;

        return null;
    }

    /**
     * Delete row by id
     *
     * @param idRow Id row
     */
    @Override
    @Transactional
    public void delete(Long idRow) {
//        log.info("IN TableValuesServiceImpl delete {}", idRow);
//       // Session session = SessionFactoryConfig.getCurrentSessionFromConfig();
//        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
//
//        Query queryIdValues = session.createQuery("select v.id from Values v where v.idRow = " + idRow);
//        List<Long> listId = queryIdValues.list();
//        listId.stream()
//                .forEach(idValue -> valuesRepository.deleteById(idValue));
//        rowValuesRepositiry.deleteById(idRow);
    }

    private Map<String, StructureDictionary> getDictionaryFields(Long idDictionary) {
        return structureDictionaryRepository.findByIdDictionary(idDictionary)
            .collect(Collectors.toMap(StructureDictionary::getName, field -> field));
    }

    private List<FieldValue> searchRow(Long dictionaryId, Long rowId, Map<Long, StructureDictionary> fields) {
        return valuesRepository.searchByRowId(dictionaryId, rowId)
                .map(value -> valueToFieldValue(value, fields))
                .collect(Collectors.toList());
    }

    private FieldValue valueToFieldValue(Values value, Map<Long, StructureDictionary> fields) {
        StructureDictionary field = fields.get(value.getIdField());

        return FieldValue.builder()
            .idField(value.getIdField())
            .name(field.getName())
            .dataTypes(field.getDataType())
            .idRow(value.getIdRow())
            .value(value.getValue())
            .build();
    }

    /**
     * Convert enum_condition in condition
     *
     * @param searchCondition Condition for search {@link SearchCondition}
     */
    private String conditionSearchToString(SearchCondition searchCondition) {
        String result = null;
        switch (searchCondition) {
            case LESS:
                result = "<";
                break;
            case LIKE:
                result = "LIKE";
                break;
            case EQUAL:
                result = "=";
                break;
            case GREATER:
                result = ">";
                break;
            case LESS_OR_EQUAL:
                result = "<=";
                break;
            case GREATER_OR_EQUAL:
                result = ">=";
                break;
        }

        return result;
    }
}
