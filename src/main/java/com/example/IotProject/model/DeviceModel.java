package com.example.IotProject.model;

import java.time.LocalDateTime;
import java.util.List;

import com.example.IotProject.enums.DeviceStatus;
import com.example.IotProject.enums.DeviceSubType;
import com.example.IotProject.enums.DeviceType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "devices")
@Getter
@Setter
public class DeviceModel {
    @Id
    private String feedName; // New primary key

    private String deviceName;

    @Enumerated(EnumType.STRING)
    @Column(name = "subtype")
    private DeviceSubType subType;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private DeviceType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private DeviceStatus status;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "zone_id")
    private ZoneModel zone;

    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL)
    private List<DeviceLogModel> deviceLogs;

    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL)
    private List<RuleModel> rules;

    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL)
    private List<DeviceDataModel> deviceData;

    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL)
    private List<ManagementModel> managements;
}