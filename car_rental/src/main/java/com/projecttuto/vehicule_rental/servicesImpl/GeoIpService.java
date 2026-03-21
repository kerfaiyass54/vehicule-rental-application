package com.projecttuto.vehicule_rental.servicesImpl;



import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;
import com.projecttuto.vehicule_rental.records.GeoLocation;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;

@Service
public class GeoIpService {

    private final DatabaseReader reader;

    public GeoIpService() throws IOException {
        InputStream db = new ClassPathResource(
                "GeoIP2-City.mmdb"
        ).getInputStream();

        reader = new DatabaseReader.Builder(db).build();
    }

    public GeoLocation resolve(String ip) {
        try {
            InetAddress address = InetAddress.getByName(ip);
            CityResponse response = reader.city(address);

            return new GeoLocation(
                    response.getCountry().getName(),
                    response.getCity().getName()
            );
        } catch (Exception e) {
            return new GeoLocation("UNKNOWN", "UNKNOWN");
        }
    }
}

