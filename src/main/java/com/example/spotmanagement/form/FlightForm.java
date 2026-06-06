package com.example.spotmanagement.form;

import lombok.Data;

@Data
public class FlightForm {

    // 到着便情報
    private Long arrSpotId;
    private Long arrAircraftTypeId;
    private String arrFlightNumber;
    private String arrFromAirport;
    private String arrScheduledDepartureTime;  // 出発予定時刻
    private String arrToAirport;
    private String arrScheduledArrivalTime;    // 到着予定時刻
    private String arrActualDepartureTime;     // 出発実績時刻（任意）
    private String arrActualArrivalTime;       // 到着実績時刻（任意）

    // 出発便情報
    private Long depSpotId;
    private Long depAircraftTypeId;
    private String depFlightNumber;
    private String depFromAirport;
    private String depScheduledDepartureTime;  // 出発予定時刻
    private String depToAirport;
    private String depScheduledArrivalTime;    // 到着予定時刻
    private String depActualDepartureTime;     // 出発実績時刻（任意）
    private String depActualArrivalTime;       // 到着実績時刻（任意）
}