package ru.itis.kpfu.mockdataserver.controllers;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.kpfu.mockdataserver.entity.server.PluginResponse;
import ru.itis.kpfu.mockdataserver.service.PluginDataService;

@RestController
@RequestMapping("")
public class PluginDataController {

    @Autowired
    private PluginDataService pluginDataService;

    @PostMapping("/add_new_endpoint")
    public ResponseEntity<String> proceedPluginData(@RequestBody PluginResponse pluginResponse) {
        try {
            String savingResultMessage = pluginDataService.savePluginData(pluginResponse);
            if (savingResultMessage.equals(pluginDataService.STATUS_SUCCESS)) {
                StringBuilder responseBuilder = new StringBuilder();
                if (pluginResponse.getLocale().equals("Russian")) {
                    responseBuilder.append("Endpoint успешно сохранен. Url для получения данных: ");
                } else responseBuilder.append("The endpoint was saved successfully. Url to get data: ");
                responseBuilder.append("http://localhost:8080/generator/");
                responseBuilder.append(pluginResponse.getUserId());
                if (!pluginResponse.getEndpoint().startsWith("/")) responseBuilder.append("/");
                responseBuilder.append(pluginResponse.getEndpoint());
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body((new Gson().toJson(responseBuilder.toString())));
            } else return ResponseEntity.badRequest().body(savingResultMessage);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Неправильный запрос");
        }
    }
}
