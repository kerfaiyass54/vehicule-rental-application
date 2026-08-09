package com.projecttuto.vehicule_rental.DTO;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor

public class SessionDTO {
    private String id;
    private String userId;
    private String username;
    private String email;
    private Instant sessionStart;
    private String ipAddress;
    private String userAgent;
    private String deviceType;
    private String country;
    private String city;
    private double riskScore;
    private boolean suspicious;
    private String suspiciousReason;
}
