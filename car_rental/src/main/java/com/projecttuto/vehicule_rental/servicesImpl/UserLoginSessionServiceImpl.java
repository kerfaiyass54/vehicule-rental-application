package com.projecttuto.vehicule_rental.servicesImpl;

import com.projecttuto.vehicule_rental.DTO.SessionDTO;
import com.projecttuto.vehicule_rental.entities.UserLoginSession;
import com.projecttuto.vehicule_rental.records.AiBehaviorRequest;
import com.projecttuto.vehicule_rental.records.AiResult;
import com.projecttuto.vehicule_rental.records.GeoLocation;
import com.projecttuto.vehicule_rental.repositories.UserLoginSessionRepository;
import com.projecttuto.vehicule_rental.services.UserLoginSessionService;
import com.projecttuto.vehicule_rental.utils.JwtUtils;
import com.projecttuto.vehicule_rental.utils.RequestUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;



@Service
public class UserLoginSessionServiceImpl implements UserLoginSessionService {

    private final UserLoginSessionRepository userLoginSessionRepository;
    private final JwtUtils jwtUtils;
    private final GeoIpService geoIpService;
    private final BehaviorFeatureService behaviorFeatureService;
    private final AiClient aiClient;

    @Value("${ip.address}")
    private String ipAddress;

    public UserLoginSessionServiceImpl(UserLoginSessionRepository repository,JwtUtils jwtUtils,GeoIpService geoIpService,BehaviorFeatureService behaviorFeatureService,
                                       AiClient aiClient) {
        this.userLoginSessionRepository = repository;
        this.jwtUtils = jwtUtils;
        this.geoIpService = geoIpService;
        this.behaviorFeatureService = behaviorFeatureService;
        this.aiClient = aiClient;
    }

    public SessionDTO mapToDTO(UserLoginSession userLoginSession) {
        SessionDTO sessionDTO = new SessionDTO();
        sessionDTO.setEmail(userLoginSession.getEmail());
        sessionDTO.setUsername(userLoginSession.getUsername());
        sessionDTO.setId(userLoginSession.getId());
        sessionDTO.setUserId(userLoginSession.getUserId());
        sessionDTO.setSessionStart(userLoginSession.getSessionStart());
        sessionDTO.setIpAddress(userLoginSession.getIpAddress());
        sessionDTO.setUserAgent(userLoginSession.getUserAgent());
        sessionDTO.setDeviceType(userLoginSession.getDeviceType());
        sessionDTO.setCountry(userLoginSession.getCountry());
        sessionDTO.setCity(userLoginSession.getCity());
        sessionDTO.setRiskScore(userLoginSession.getRiskScore());
        sessionDTO.setSuspicious(userLoginSession.isSuspicious());
        sessionDTO.setSuspiciousReason(userLoginSession.getSuspiciousReason());
        return sessionDTO;
    }

    @Override
    public void saveSession(HttpServletRequest request){

        String sessionId = jwtUtils.sessionId();

        if (userLoginSessionRepository.existsBySessionId(sessionId)) {
            return;
        }

        String ip = RequestUtils.getClientIp(request);
        String ua = RequestUtils.getUserAgent(request);
        GeoLocation geo = geoIpService.resolve(ip);
        if ("0:0:0:0:0:0:0:1".equals(ip) || "127.0.0.1".equals(ip)) {
            ip = ipAddress;
        }
        UserLoginSession session = new UserLoginSession();
        session.setUserId(jwtUtils.userId());
        session.setUsername(jwtUtils.username());
        session.setEmail(jwtUtils.email());
        session.setSessionId(sessionId);
        session.setSessionStart(Instant.now());
        session.setIpAddress(ip);
        session.setUserAgent(ua);
        session.setCountry(geo.country());
        session.setCity(geo.city());

        List<UserLoginSession> history = userLoginSessionRepository.findUserLoginSessionsByUserId(session.getUserId());
        if(history != null){
            AiBehaviorRequest features = behaviorFeatureService.buildFeatures(session, history);
            AiResult ai = aiClient.analyze(features);
            session.setRiskScore(ai.riskScore());
            session.setSuspicious(ai.suspicious());
            session.setSuspiciousReason(
                    ai.suspicious() ? "AI anomaly detected" : null
            );
        }
        else{
            session.setRiskScore(0);
            session.setSuspicious(false);
            session.setSuspiciousReason(
                    null
            );
        }
        userLoginSessionRepository.save(session);
    }

    @Override
    public Page<SessionDTO> findAllUseLoginSessionsByEmailPage(String email, int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        return userLoginSessionRepository.findUserLoginSessionByEmail(email,pageable).map(this::mapToDTO);
    }



    @Override
    public SessionDTO getSession(String id){
        UserLoginSession session = userLoginSessionRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Session not found with id: " + id)
                );

        return mapToDTO(session);
    }


}
