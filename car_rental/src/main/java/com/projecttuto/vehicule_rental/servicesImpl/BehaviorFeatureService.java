package com.projecttuto.vehicule_rental.servicesImpl;

import com.projecttuto.vehicule_rental.entities.UserLoginSession;
import com.projecttuto.vehicule_rental.records.AiBehaviorRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
public class BehaviorFeatureService {

    public AiBehaviorRequest buildFeatures(
            UserLoginSession current,
            List<UserLoginSession> history
    ) {
        int countryCount =
                (int) history.stream()
                        .map(UserLoginSession::getCountry)
                        .distinct()
                        .count();

        int deviceCount =
                (int) history.stream()
                        .map(UserLoginSession::getUserAgent)
                        .distinct()
                        .count();

        int hour = LocalDateTime
                .ofInstant(current.getSessionStart(), ZoneId.systemDefault())
                .getHour();

        return new AiBehaviorRequest(countryCount, deviceCount, hour);
    }
}

