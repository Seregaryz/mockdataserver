package ru.itis.kpfu.mockdataserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.kpfu.mockdataserver.entity.dao.HistoryEndpoint;
import ru.itis.kpfu.mockdataserver.entity.server.EndpointItem;
import ru.itis.kpfu.mockdataserver.repository.EndpointRepository;
import ru.itis.kpfu.mockdataserver.repository.HistoryEndpointRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class RequestHistoryService {

    @Autowired
    private EndpointRepository endpointRepository;

    @Autowired
    private HistoryEndpointRepository historyEndpointRepository;

    public List<EndpointItem> getEndpointList(String userId) {
        return endpointRepository.findAllByAdditionalPath(userId).stream()
            .map(endpoint -> new EndpointItem(endpoint.getId(), endpoint.getUserPath())).collect(Collectors.toList());
    }

    public void saveEndpointToHistory(String data, String userId, String path) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.getDefault());
        HistoryEndpoint historyEndpoint = new HistoryEndpoint();
        historyEndpoint.setData(data);
        historyEndpoint.setUserId(userId);
        historyEndpoint.setPath(path);
        historyEndpoint.setSendDate(format.format(new Date()));
        historyEndpointRepository.save(historyEndpoint);
    }

    public List<HistoryEndpoint> getHistoryEndpointList(String userId) {
        return historyEndpointRepository.findAllByUserId(userId);
    }

}
