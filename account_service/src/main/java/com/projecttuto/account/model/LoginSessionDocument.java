package com.projecttuto.account.model;

import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "account-login-sessions")
public class LoginSessionDocument {
    @Id private String id;
    private String userId;
    private String username;
    private String email;
    private String sessionId;
    @Field(type = FieldType.Date) private Instant sessionStart;
    private String ipAddress;
    private String userAgent;
    private String deviceType;
    private String country;
    private String city;
    private double riskScore;
    private boolean suspicious;
    private String suspiciousReason;
    public String getId(){return id;} public void setId(String v){id=v;}
    public String getUserId(){return userId;} public void setUserId(String v){userId=v;}
    public String getUsername(){return username;} public void setUsername(String v){username=v;}
    public String getEmail(){return email;} public void setEmail(String v){email=v;}
    public String getSessionId(){return sessionId;} public void setSessionId(String v){sessionId=v;}
    public Instant getSessionStart(){return sessionStart;} public void setSessionStart(Instant v){sessionStart=v;}
    public String getIpAddress(){return ipAddress;} public void setIpAddress(String v){ipAddress=v;}
    public String getUserAgent(){return userAgent;} public void setUserAgent(String v){userAgent=v;}
    public String getDeviceType(){return deviceType;} public void setDeviceType(String v){deviceType=v;}
    public String getCountry(){return country;} public void setCountry(String v){country=v;}
    public String getCity(){return city;} public void setCity(String v){city=v;}
    public double getRiskScore(){return riskScore;} public void setRiskScore(double v){riskScore=v;}
    public boolean isSuspicious(){return suspicious;} public void setSuspicious(boolean v){suspicious=v;}
    public String getSuspiciousReason(){return suspiciousReason;} public void setSuspiciousReason(String v){suspiciousReason=v;}
}
