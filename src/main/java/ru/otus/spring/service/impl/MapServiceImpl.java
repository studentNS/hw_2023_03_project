package ru.otus.spring.service.impl;

import com.jayway.jsonpath.JsonPath;
import org.springframework.stereotype.Service;
import ru.otus.spring.service.MapService;

import java.util.ArrayList;
import java.util.List;

@Service
public class MapServiceImpl implements MapService {

    private static final String EMOTICON_START_STRING = "\uD83D\uDCCD";

    @Override
    public String getResultSearchFromJson(String jsonStr) {
        Integer countResult = JsonPath.read(jsonStr, "$.properties.ResponseMetaData.SearchResponse.found");
        if (countResult > 10) {
            countResult = 10;
        }
        List<String> resultOrgNameArr = new ArrayList<>();
        for (int i = 0; i < countResult; i++) {
            String orgName = JsonPath.read(jsonStr, "$.features[" + i + "].properties.name");
            String orgAddr = JsonPath.read(jsonStr, "$.features[" + i + "].properties.description");
            resultOrgNameArr.add(orgName + ", " + orgAddr);
        }
        return transformListToString(resultOrgNameArr);
    }

    public String transformListToString(List<String> result) {
        String resultStr = EMOTICON_START_STRING;
        for (int i = 0; i < result.size(); i++) {
            resultStr = resultStr + result.get(i);
            if (i != result.size() - 1) {
                resultStr = resultStr + "\n";
                resultStr = resultStr + EMOTICON_START_STRING;
            }
        }
        return resultStr;
    }
}
