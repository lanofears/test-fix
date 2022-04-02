package ru.kolchunov.sberver2.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.kolchunov.sberver2.models.DataTypes;
import ru.kolchunov.sberver2.requests.CreateDictRequest;
import ru.kolchunov.sberver2.responses.DictionaryModel;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
class DictionaryServiceImplTest {

    @Autowired
    DictionaryService dictionaryService;

    @Test
    void saveNewStructure() {
        CreateDictRequest createDictRequest = new CreateDictRequest();
        createDictRequest.setCode("TEST_CODE");
        createDictRequest.setName("SOME NAME");
        List<CreateDictRequest.FieldStructure> fieldsStructure = new ArrayList<>();
        fieldsStructure.add(new CreateDictRequest.FieldStructure("first_long_field", DataTypes.LONG));
        fieldsStructure.add(new CreateDictRequest.FieldStructure("second_boolean_field", DataTypes.BOOLEAN));
        fieldsStructure.add(new CreateDictRequest.FieldStructure("third_string_field", DataTypes.STRING));
        fieldsStructure.add(new CreateDictRequest.FieldStructure("fourth_date_field", DataTypes.DATE));
        createDictRequest.setFieldsStructure(fieldsStructure);

        DictionaryModel dictionary = dictionaryService.create(createDictRequest);
        Assertions.assertNotNull(dictionary.getId());
        Assertions.assertEquals("TEST_CODE", dictionary.getCode());
        Assertions.assertEquals("SOME NAME", dictionary.getName());
        Assertions.assertEquals(4, dictionary.getFields().size());
    }
}