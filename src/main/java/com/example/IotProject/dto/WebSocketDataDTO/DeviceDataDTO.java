package com.example.IotProject.dto.WebSocketDataDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceDataDTO {
    private String feedName;
    private Timestamp timestamp;
    private Float value;

    @Override
    public String toString() {
        return "DeviceDataDTO{" +
                "feedName='" + feedName + '\'' +
                ", timestamp=" + timestamp +
                ", value=" + value +
                '}';
    }
}
