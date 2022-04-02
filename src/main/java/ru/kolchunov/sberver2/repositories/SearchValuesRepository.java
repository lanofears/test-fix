package ru.kolchunov.sberver2.repositories;

import ru.kolchunov.sberver2.models.Values;

import java.util.stream.Stream;

/**
 * Это пример как выполнять поиск по кастомному запросу в том случае когда нужен доступ
 * к EntityManager для создания динамически формируемого запроса
 */
public interface SearchValuesRepository {
    Stream<Values> searchByRowId(Long dictionaryId, Long rowId);
}
