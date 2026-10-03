package com.projecttuto.account.repository;

import com.projecttuto.account.model.LoginSessionDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface LoginSessionRepository extends ElasticsearchRepository<LoginSessionDocument,String> {
    Page<LoginSessionDocument> findByEmail(String email, Pageable pageable);
    boolean existsBySessionId(String sessionId);
}
