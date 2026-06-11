package com.example.spotmanagement;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.spotmanagement.entity.AircraftType;
import com.example.spotmanagement.entity.Flight;
import com.example.spotmanagement.entity.Spot;
import com.example.spotmanagement.entity.User;
import com.example.spotmanagement.repository.AircraftTypeRepository;
import com.example.spotmanagement.repository.FlightRepository;
import com.example.spotmanagement.repository.SpotRepository;
import com.example.spotmanagement.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SpotRepository spotRepository;
    private final AircraftTypeRepository aircraftTypeRepository;
    private final FlightRepository flightRepository;

    @Override
    public void run(String... args) throws Exception {
        // ユーザーの初期データ
        if (userRepository.count() == 0) {
            User user = new User();
            user.setUsername("admin");
            user.setPassword(passwordEncoder.encode("admin123"));
            user.setCreatedAt(LocalDateTime.now());
            userRepository.save(user);
            System.out.println("テストユーザーを作成しました: admin / admin123");
        }

        // スポットの初期データ
        if (spotRepository.count() == 0) {
            String[][] spotData = {
                {"144", "W44"},
                {"145", "W44"},
                {"146", "W36"},
                {"147", "W36"},
                {"148", "W28"}
            };
            for (String[] data : spotData) {
                Spot spot = new Spot();
                spot.setSpotNumber(data[0]);
                spot.setMaxWingspan(data[1]);
                spot.setCreatedAt(LocalDateTime.now());
                spotRepository.save(spot);
            }
            System.out.println("スポットの初期データを作成しました");
        }

        // 型式の初期データ
        if (aircraftTypeRepository.count() == 0) {
            String[][] typeData = {
                {"B777", "W32"},
                {"B787", "W30"},
                {"B767", "W24"},
                {"B737", "W18"},
                {"A320", "W18"},
                {"A321", "W18"}
            };
            for (String[] data : typeData) {
                AircraftType type = new AircraftType();
                type.setTypeName(data[0]);
                type.setWingspan(data[1]);
                type.setCreatedAt(LocalDateTime.now());
                aircraftTypeRepository.save(type);
            }
            System.out.println("型式の初期データを作成しました");
        }
        
     // 便の初期データ
        if (flightRepository.count() == 0) {
            Spot spot144 = spotRepository.findAll().get(0);
            Spot spot145 = spotRepository.findAll().get(1);
            AircraftType b777 = aircraftTypeRepository.findAll().get(0);
            AircraftType b737 = aircraftTypeRepository.findAll().get(3);

         // 今日の日付を取得
            LocalDate today = LocalDate.now();

            Flight flight1 = new Flight();
            flight1.setArrSpot(spot144);
            flight1.setArrAircraftType(b777);
            flight1.setArrFlightNumber("NH96");
            flight1.setArrFromAirport("VHHH");
            flight1.setArrScheduledDepartureTime(today.atTime(1, 0));
            flight1.setArrToAirport("RJTT");
            flight1.setArrScheduledArrivalTime(today.atTime(3, 0));
            flight1.setDepSpot(spot144);
            flight1.setDepAircraftType(b777);
            flight1.setDepFlightNumber("NH216");
            flight1.setDepFromAirport("RJTT");
            flight1.setDepScheduledDepartureTime(today.atTime(8, 0));
            flight1.setDepToAirport("ROAH");
            flight1.setDepScheduledArrivalTime(today.atTime(10, 30));
            flight1.setSizeWarning(false);
            flight1.setIntervalWarning(false);
            flight1.setCreatedAt(LocalDateTime.now());
            flightRepository.save(flight1);

            Flight flight2 = new Flight();
            flight2.setArrSpot(spot145);
            flight2.setArrAircraftType(b737);
            flight2.setArrFlightNumber("NH101");
            flight2.setArrFromAirport("RJSS");
            flight2.setArrScheduledDepartureTime(today.atTime(4, 0));
            flight2.setArrToAirport("RJTT");
            flight2.setArrScheduledArrivalTime(today.atTime(5, 30));
            flight2.setDepSpot(spot145);
            flight2.setDepAircraftType(b737);
            flight2.setDepFlightNumber("NH202");
            flight2.setDepFromAirport("RJTT");
            flight2.setDepScheduledDepartureTime(today.atTime(7, 0));
            flight2.setDepToAirport("RJCC");
            flight2.setDepScheduledArrivalTime(today.atTime(9, 0));
            flight2.setSizeWarning(false);
            flight2.setIntervalWarning(false);
            flight2.setCreatedAt(LocalDateTime.now());
            flightRepository.save(flight2);

            System.out.println("便の初期データを作成しました");
        }
    }
}