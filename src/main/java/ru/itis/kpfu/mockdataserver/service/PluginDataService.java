package ru.itis.kpfu.mockdataserver.service;

import com.google.gson.internal.LinkedTreeMap;
import org.springframework.stereotype.Service;
import ru.itis.kpfu.mockdataserver.entity.server.FieldType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class PluginDataService {

    public HashMap<String, FieldType> parseJsonModelMap(HashMap<String, LinkedTreeMap<String, Object>> jsonModelMap) {
        HashMap<String, FieldType> modelMap = new HashMap<>();
        jsonModelMap.keySet().forEach(fieldKey -> {
            modelMap.put(fieldKey, new FieldType(
                (String) jsonModelMap.get(fieldKey).get("type"),
                (Boolean) jsonModelMap.get(fieldKey).get("isPrimitive")
            ));
        });
        return modelMap;
    }

    public List<HashMap<String, HashMap<String, FieldType>>> parseJsonAdditionalModelMap(
        List<LinkedTreeMap<String, LinkedTreeMap<String, LinkedTreeMap<String, Object>>>> jsonAdditionalModels
    ) {
        List<HashMap<String, HashMap<String, FieldType>>> additionalModelList = new ArrayList<>();
        jsonAdditionalModels.forEach(classMap -> {
            HashMap<String, HashMap<String, FieldType>> resultClassMap = new HashMap<>();
            classMap.keySet().forEach(classKey -> {
                HashMap<String, FieldType> resultMap = new HashMap<>();
                classMap.get(classKey).keySet().forEach(fieldKey -> {
                    resultMap.put(fieldKey, new FieldType(
                        (String) classMap.get(classKey).get(fieldKey).get("type"),
                        (Boolean) classMap.get(classKey).get(fieldKey).get("isPrimitive")
                    ));
                });
                resultClassMap.put(classKey, resultMap);
            });
            additionalModelList.add(resultClassMap);
        });
        return additionalModelList;
    }
}
