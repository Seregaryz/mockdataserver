package ru.itis.kpfu.mockdataserver.entity.server;

import java.io.Serializable;

public class EndpointItem implements Serializable {

    private long id;

    private String path;

    public EndpointItem(long id, String path) {
        this.id = id;
        this.path = path;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
