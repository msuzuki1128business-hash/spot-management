package com.example.spotmanagement.controller;

import com.example.spotmanagement.entity.AircraftType;
import com.example.spotmanagement.entity.Flight;
import com.example.spotmanagement.entity.Spot;
import com.example.spotmanagement.form.FlightForm;
import com.example.spotmanagement.repository.AircraftTypeRepository;
import com.example.spotmanagement.repository.FlightRepository;
import com.example.spotmanagement.repository.SpotRepository;
import com.example.spotmanagement.service.FlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class SpotController {

    private final SpotRepository spotRepository;
    private final FlightRepository flightRepository;
    private final AircraftTypeRepository aircraftTypeRepository;
    private final FlightService flightService;

    @GetMapping("/spots")
    public String showSpots(Model model) {
        List<Spot> spots = spotRepository.findAll();
        List<Flight> flights = flightRepository.findAll();
        model.addAttribute("spots", spots);
        model.addAttribute("flights", flights);
        return "spots/index";
    }

    @GetMapping("/spots/flights/new")
    public String showFlightForm(Model model) {
        List<Spot> spots = spotRepository.findAll();
        List<AircraftType> aircraftTypes = aircraftTypeRepository.findAll();
        model.addAttribute("spots", spots);
        model.addAttribute("aircraftTypes", aircraftTypes);
        model.addAttribute("flightForm", new FlightForm());
        return "spots/flight_form";
    }

    @PostMapping("/spots/flights")
    public String createFlight(@ModelAttribute FlightForm form,
                               RedirectAttributes redirectAttributes,
                               Model model) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

        Spot arrSpot = spotRepository.findById(form.getArrSpotId()).orElseThrow();
        Spot depSpot = spotRepository.findById(form.getDepSpotId()).orElseThrow();
        AircraftType arrAircraftType = aircraftTypeRepository.findById(form.getArrAircraftTypeId()).orElseThrow();
        AircraftType depAircraftType = aircraftTypeRepository.findById(form.getDepAircraftTypeId()).orElseThrow();

        LocalDateTime arrivalTime = LocalDateTime.parse(form.getArrScheduledArrivalTime(), formatter);
        LocalDateTime departureTime = LocalDateTime.parse(form.getDepScheduledDepartureTime(), formatter);

        // ② 時間重複チェック（エラー：登録不可）
        if (flightService.isOverlap(arrSpot.getId(), arrivalTime, departureTime, null)) {
            List<Spot> spots = spotRepository.findAll();
            List<AircraftType> aircraftTypes = aircraftTypeRepository.findAll();
            model.addAttribute("spots", spots);
            model.addAttribute("aircraftTypes", aircraftTypes);
            model.addAttribute("flightForm", form);
            model.addAttribute("errorMessage", "指定した時間帯はすでに使用されています");
            return "spots/flight_form";
        }

        Flight flight = new Flight();
        flight.setArrSpot(arrSpot);
        flight.setArrAircraftType(arrAircraftType);
        flight.setArrFlightNumber(form.getArrFlightNumber());
        flight.setArrFromAirport(form.getArrFromAirport());
        flight.setArrScheduledDepartureTime(LocalDateTime.parse(form.getArrScheduledDepartureTime(), formatter));
        flight.setArrToAirport(form.getArrToAirport());
        flight.setArrScheduledArrivalTime(arrivalTime);
        flight.setDepSpot(depSpot);
        flight.setDepAircraftType(depAircraftType);
        flight.setDepFlightNumber(form.getDepFlightNumber());
        flight.setDepFromAirport(form.getDepFromAirport());
        flight.setDepScheduledDepartureTime(departureTime);
        flight.setDepToAirport(form.getDepToAirport());
        flight.setDepScheduledArrivalTime(LocalDateTime.parse(form.getDepScheduledArrivalTime(), formatter));

        if (form.getArrActualDepartureTime() != null && !form.getArrActualDepartureTime().isEmpty()) {
            flight.setArrActualDepartureTime(LocalDateTime.parse(form.getArrActualDepartureTime(), formatter));
        }
        if (form.getArrActualArrivalTime() != null && !form.getArrActualArrivalTime().isEmpty()) {
            flight.setArrActualArrivalTime(LocalDateTime.parse(form.getArrActualArrivalTime(), formatter));
        }
        if (form.getDepActualDepartureTime() != null && !form.getDepActualDepartureTime().isEmpty()) {
            flight.setDepActualDepartureTime(LocalDateTime.parse(form.getDepActualDepartureTime(), formatter));
        }
        if (form.getDepActualArrivalTime() != null && !form.getDepActualArrivalTime().isEmpty()) {
            flight.setDepActualArrivalTime(LocalDateTime.parse(form.getDepActualArrivalTime(), formatter));
        }

        flight.setCreatedAt(LocalDateTime.now());

        // ① 型式サイズチェック（警告フラグをセット）
        boolean sizeWarning = flightService.isSizeOver(arrSpot, arrAircraftType);

        // ③ 30分インターバルチェック（警告フラグをセット）
        boolean intervalWarning = flightService.isIntervalShort(arrSpot.getId(), arrivalTime, departureTime, null);

        flight.setSizeWarning(sizeWarning);
        flight.setIntervalWarning(intervalWarning);

        flightRepository.save(flight);

        return "redirect:/spots";
    }
    
    @GetMapping("/spots/flights/{id}/edit")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Flight flight = flightRepository.findById(id).orElseThrow();
        List<Spot> spots = spotRepository.findAll();
        List<AircraftType> aircraftTypes = aircraftTypeRepository.findAll();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

        FlightForm form = new FlightForm();
        form.setArrSpotId(flight.getArrSpot().getId());
        form.setArrAircraftTypeId(flight.getArrAircraftType().getId());
        form.setArrFlightNumber(flight.getArrFlightNumber());
        form.setArrFromAirport(flight.getArrFromAirport());
        form.setArrScheduledDepartureTime(flight.getArrScheduledDepartureTime().format(formatter));
        form.setArrToAirport(flight.getArrToAirport());
        form.setArrScheduledArrivalTime(flight.getArrScheduledArrivalTime().format(formatter));
        form.setDepSpotId(flight.getDepSpot().getId());
        form.setDepAircraftTypeId(flight.getDepAircraftType().getId());
        form.setDepFlightNumber(flight.getDepFlightNumber());
        form.setDepFromAirport(flight.getDepFromAirport());
        form.setDepScheduledDepartureTime(flight.getDepScheduledDepartureTime().format(formatter));
        form.setDepToAirport(flight.getDepToAirport());
        form.setDepScheduledArrivalTime(flight.getDepScheduledArrivalTime().format(formatter));

        if (flight.getArrActualDepartureTime() != null) {
            form.setArrActualDepartureTime(flight.getArrActualDepartureTime().format(formatter));
        }
        if (flight.getArrActualArrivalTime() != null) {
            form.setArrActualArrivalTime(flight.getArrActualArrivalTime().format(formatter));
        }
        if (flight.getDepActualDepartureTime() != null) {
            form.setDepActualDepartureTime(flight.getDepActualDepartureTime().format(formatter));
        }
        if (flight.getDepActualArrivalTime() != null) {
            form.setDepActualArrivalTime(flight.getDepActualArrivalTime().format(formatter));
        }

        model.addAttribute("spots", spots);
        model.addAttribute("aircraftTypes", aircraftTypes);
        model.addAttribute("flightForm", form);
        model.addAttribute("flightId", id);
        return "spots/flight_edit";
    }

    @PostMapping("/spots/flights/{id}")
    public String updateFlight(@PathVariable("id") Long id,
                               @ModelAttribute FlightForm form,
                               Model model) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

        Spot arrSpot = spotRepository.findById(form.getArrSpotId()).orElseThrow();
        Spot depSpot = spotRepository.findById(form.getDepSpotId()).orElseThrow();
        AircraftType arrAircraftType = aircraftTypeRepository.findById(form.getArrAircraftTypeId()).orElseThrow();
        AircraftType depAircraftType = aircraftTypeRepository.findById(form.getDepAircraftTypeId()).orElseThrow();

        LocalDateTime arrivalTime = LocalDateTime.parse(form.getArrScheduledArrivalTime(), formatter);
        LocalDateTime departureTime = LocalDateTime.parse(form.getDepScheduledDepartureTime(), formatter);

        // 時間重複チェック（自分自身は除外）
        if (flightService.isOverlap(arrSpot.getId(), arrivalTime, departureTime, id)) {
            List<Spot> spots = spotRepository.findAll();
            List<AircraftType> aircraftTypes = aircraftTypeRepository.findAll();
            model.addAttribute("spots", spots);
            model.addAttribute("aircraftTypes", aircraftTypes);
            model.addAttribute("flightForm", form);
            model.addAttribute("flightId", id);
            model.addAttribute("errorMessage", "指定した時間帯はすでに使用されています");
            return "spots/flight_edit";
        }

        Flight flight = flightRepository.findById(id).orElseThrow();
        flight.setArrSpot(arrSpot);
        flight.setArrAircraftType(arrAircraftType);
        flight.setArrFlightNumber(form.getArrFlightNumber());
        flight.setArrFromAirport(form.getArrFromAirport());
        flight.setArrScheduledDepartureTime(LocalDateTime.parse(form.getArrScheduledDepartureTime(), formatter));
        flight.setArrToAirport(form.getArrToAirport());
        flight.setArrScheduledArrivalTime(arrivalTime);
        flight.setDepSpot(depSpot);
        flight.setDepAircraftType(depAircraftType);
        flight.setDepFlightNumber(form.getDepFlightNumber());
        flight.setDepFromAirport(form.getDepFromAirport());
        flight.setDepScheduledDepartureTime(departureTime);
        flight.setDepToAirport(form.getDepToAirport());
        flight.setDepScheduledArrivalTime(LocalDateTime.parse(form.getDepScheduledArrivalTime(), formatter));

        if (form.getArrActualDepartureTime() != null && !form.getArrActualDepartureTime().isEmpty()) {
            flight.setArrActualDepartureTime(LocalDateTime.parse(form.getArrActualDepartureTime(), formatter));
        }
        if (form.getArrActualArrivalTime() != null && !form.getArrActualArrivalTime().isEmpty()) {
            flight.setArrActualArrivalTime(LocalDateTime.parse(form.getArrActualArrivalTime(), formatter));
        }
        if (form.getDepActualDepartureTime() != null && !form.getDepActualDepartureTime().isEmpty()) {
            flight.setDepActualDepartureTime(LocalDateTime.parse(form.getDepActualDepartureTime(), formatter));
        }
        if (form.getDepActualArrivalTime() != null && !form.getDepActualArrivalTime().isEmpty()) {
            flight.setDepActualArrivalTime(LocalDateTime.parse(form.getDepActualArrivalTime(), formatter));
        }

        boolean sizeWarning = flightService.isSizeOver(arrSpot, arrAircraftType);
        boolean intervalWarning = flightService.isIntervalShort(arrSpot.getId(), arrivalTime, departureTime, id);
        flight.setSizeWarning(sizeWarning);
        flight.setIntervalWarning(intervalWarning);

        flightRepository.save(flight);
        return "redirect:/spots";
    }

    @PostMapping("/spots/flights/{id}/delete")
    public String deleteFlight(@PathVariable("id") Long id) {
        flightRepository.deleteById(id);
        return "redirect:/spots";
    }
    
}