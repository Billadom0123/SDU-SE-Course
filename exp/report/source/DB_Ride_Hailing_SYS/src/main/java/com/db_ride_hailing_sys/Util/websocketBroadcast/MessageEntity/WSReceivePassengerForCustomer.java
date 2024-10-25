package com.db_ride_hailing_sys.Util.websocketBroadcast.MessageEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WSReceivePassengerForCustomer {
    String reqId;
    Boolean receivePassenger;
}
