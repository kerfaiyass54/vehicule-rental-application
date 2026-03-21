package com.projecttuto.vehicule_rental.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@Document(indexName = "user_login_sessions")
public class UserLoginSession {

    @Id
    private String id;
    private String userId;
    private String username;
    private String email;
    private String sessionId;
    @Field(type = FieldType.Date)
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