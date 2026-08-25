//package com.projecttuto.vehicule_rental.servicesImpl;
//
//import com.projecttuto.vehicule_rental.dto.SessionDTO;
//import com.projecttuto.vehicule_rental.entities.UserLoginSession;
//import com.projecttuto.vehicule_rental.exception.SessionNotFoundException;
//import com.projecttuto.vehicule_rental.records.AiBehaviorRequest;
//import com.projecttuto.vehicule_rental.records.AiResult;
//import com.projecttuto.vehicule_rental.records.GeoLocation;
//import com.projecttuto.vehicule_rental.repositories.UserLoginSessionRepository;
//import com.projecttuto.vehicule_rental.services.UserLoginSessionService;
//import com.projecttuto.vehicule_rental.utils.JwtUtils;
//import com.projecttuto.vehicule_rental.utils.RequestUtils;
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//
//import java.time.Instant;
//import java.util.List;
//
//@Service
//public class UserLoginSessionServiceImpl implements UserLoginSessionService {
//
//    private final UserLoginSessionRepository userLoginSessionRepository;
//    private final JwtUtils jwtUtils;
//    private final GeoIpService geoIpService;
//    private final BehaviorFeatureService behaviorFeatureService;
//    private final AiClient aiClient;
//
//    @Value("${ip.address}")
//    private String ipAddress;
//
//    public UserLoginSessionServiceImpl(
//            UserLoginSessionRepository repository,
//            JwtUtils jwtUtils,
//            GeoIpService geoIpService,
//            BehaviorFeatureService behaviorFeatureService,
//            AiClient aiClient) {
//
//        this.userLoginSessionRepository = repository;
//        this.jwtUtils = jwtUtils;
//        this.geoIpService = geoIpService;
//        this.behaviorFeatureService = behaviorFeatureService;
//        this.aiClient = aiClient;
//    }
//
//    @Override
//    public void saveSession(HttpServletRequest request) {
//
//        String sessionId = jwtUtils.sessionId();
//
//        if (sessionAlreadyExists(sessionId)) {
//            return;
//        }
//
//        UserLoginSession session = createSession(request, sessionId);
//
//        analyzeSession(session);
//
//        userLoginSessionRepository.save(session);
//    }
//
//    @Override
//    public Page<SessionDTO> findAllUseLoginSessionsByEmailPage(
//            String email,
//            int page,
//            int size) {
//
//        Pageable pageable = PageRequest.of(page, size);
//
//        return userLoginSessionRepository
//                .findUserLoginSessionByEmail(email, pageable)
//                .map(this::mapToDTO);
//    }
//
//    @Override
//    public SessionDTO getSession(String id) {
//
//        UserLoginSession session = findSessionById(id);
//
//        return mapToDTO(session);
//    }
//
//    /*
//     * ============================
//     * Session creation
//     * ============================
//     */
//
//    private boolean sessionAlreadyExists(String sessionId) {
//
//        return userLoginSessionRepository.existsBySessionId(sessionId);
//    }
//
//    private UserLoginSession createSession(
//            HttpServletRequest request,
//            String sessionId) {
//
//        String ip = RequestUtils.getClientIp(request);
//
//        if (isLocalhost(ip)) {
//            ip = ipAddress;
//        }
//
//        String userAgent = RequestUtils.getUserAgent(request);
//
//        GeoLocation geoLocation = geoIpService.resolve(ip);
//
//        UserLoginSession session = new UserLoginSession();
//
//        setAuthenticationData(session, sessionId);
//        setRequestData(session, ip, userAgent, geoLocation);
//
//        return session;
//    }
//
//    private void setAuthenticationData(
//            UserLoginSession session,
//            String sessionId) {
//
//        session.setUserId(jwtUtils.userId());
//        session.setUsername(jwtUtils.username());
//        session.setEmail(jwtUtils.email());
//        session.setSessionId(sessionId);
//        session.setSessionStart(Instant.now());
//    }
//
//    private void setRequestData(
//            UserLoginSession session,
//            String ip,
//            String userAgent,
//            GeoLocation geoLocation) {
//
//        session.setIpAddress(ip);
//        session.setUserAgent(userAgent);
//        session.setCountry(geoLocation.country());
//        session.setCity(geoLocation.city());
//    }
//
//    private boolean isLocalhost(String ip) {
//
//        return "0:0:0:0:0:0:0:1".equals(ip)
//                || "127.0.0.1".equals(ip);
//    }
//
//    /*
//     * ============================
//     * AI analysis
//     * ============================
//     */
//
//    private void analyzeSession(UserLoginSession session) {
//
//        List<UserLoginSession> history =
//                userLoginSessionRepository
//                        .findUserLoginSessionsByUserId(
//                                session.getUserId());
//
//        if (history != null) {
//            applyAiAnalysis(session, history);
//        } else {
//            setDefaultRiskValues(session);
//        }
//    }
//
//    private void applyAiAnalysis(
//            UserLoginSession session,
//            List<UserLoginSession> history) {
//
//        AiBehaviorRequest features =
//                behaviorFeatureService.buildFeatures(
//                        session,
//                        history);
//
//        AiResult ai =
//                aiClient.analyze(features);
//
//        session.setRiskScore(ai.riskScore());
//
//        session.setSuspicious(ai.suspicious());
//
//        session.setSuspiciousReason(
//                ai.suspicious()
//                        ? "AI anomaly detected"
//                        : null
//        );
//    }
//
//    private void setDefaultRiskValues(
//            UserLoginSession session) {
//
//        session.setRiskScore(0);
//        session.setSuspicious(false);
//        session.setSuspiciousReason(null);
//    }
//
//    /*
//     * ============================
//     * Session retrieval
//     * ============================
//     */
//
//    private UserLoginSession findSessionById(String id) {
//
//        return userLoginSessionRepository
//                .findById(id)
//                .orElseThrow(() ->
//                        new SessionNotFoundException(
//                                "Session not found with id: " + id
//                        )
//                );
//    }
//
//    /*
//     * ============================
//     * DTO mapping
//     * ============================
//     */
//
//    private SessionDTO mapToDTO(
//            UserLoginSession userLoginSession) {
//
//        SessionDTO sessionDTO = new SessionDTO();
//
//        sessionDTO.setEmail(
//                userLoginSession.getEmail());
//
//        sessionDTO.setUsername(
//                userLoginSession.getUsername());
//
//        sessionDTO.setId(
//                userLoginSession.getId());
//
//        sessionDTO.setUserId(
//                userLoginSession.getUserId());
//
//        sessionDTO.setSessionStart(
//                userLoginSession.getSessionStart());
//
//        sessionDTO.setIpAddress(
//                userLoginSession.getIpAddress());
//
//        sessionDTO.setUserAgent(
//                userLoginSession.getUserAgent());
//
//        sessionDTO.setDeviceType(
//                userLoginSession.getDeviceType());
//
//        sessionDTO.setCountry(
//                userLoginSession.getCountry());
//
//        sessionDTO.setCity(
//                userLoginSession.getCity());
//
//        sessionDTO.setRiskScore(
//                userLoginSession.getRiskScore());
//
//        sessionDTO.setSuspicious(
//                userLoginSession.isSuspicious());
//
//        sessionDTO.setSuspiciousReason(
//                userLoginSession.getSuspiciousReason());
//
//        return sessionDTO;
//    }
//}