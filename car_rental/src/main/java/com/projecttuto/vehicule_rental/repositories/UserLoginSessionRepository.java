package com.projecttuto.vehicule_rental.repositories;

import com.projecttuto.vehicule_rental.entities.UserLoginSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface UserLoginSessionRepository extends ElasticsearchRepository<UserLoginSession, String> {

    List<UserLoginSession> findUserLoginSessionByEmail(String email);

    Page<UserLoginSession>  findUserLoginSessionByEmail(String email, Pageable pageable);

    List<UserLoginSession> findUserLoginSessionBySessionStartAndUserId(Instant sessionStart,String userId);

    boolean existsByUserIdAndSessionStart(String userId, Instant sessionStart);

    List<UserLoginSession> findUserLoginSessionsByUserId(String userId);

    boolean existsBySessionId(String sessionId);

    UserLoginSession findSessionById(String Id);






}
