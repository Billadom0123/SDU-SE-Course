package com.db_ride_hailing_sys.Util.websocketBroadcast.Encoder;

import com.db_ride_hailing_sys.Util.websocketBroadcast.MessageEntity.WSRequestMessageForDriver;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class RequestForDriverEncoder implements Encoder.Text<WSRequestMessageForDriver> {
    @Override
    public String encode(WSRequestMessageForDriver wsRequestMessageForDriver) throws EncodeException {
        JsonMapper jsonMapper = new JsonMapper();
        try {
            return jsonMapper.writeValueAsString(wsRequestMessageForDriver);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
