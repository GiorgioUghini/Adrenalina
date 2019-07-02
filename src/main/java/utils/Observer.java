package utils;

import network.Response;

public interface Observer {
    void update(Response response);
}
