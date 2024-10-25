package com.db_ride_hailing_sys.Util.websocketBroadcast.MessageEntity;

import lombok.Data;

@Data
public class StringWSOrderMessageForCustomer {
    private String reqId;
    private String price;
}
