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


    Page<UserLoginSession>  findUserLoginSessionByEmail(String email, Pageable pageable);



    List<UserLoginSession> findUserLoginSessionsByUserId(String userId);

    boolean existsBySessionId(String sessionId);







}
