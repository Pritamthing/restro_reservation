package com.assesment.web.controller;

import com.assesment.persistent.model.PersistentReservationEntity;
import com.assesment.service.ReservationService;
import com.assesment.service.TableService;
import com.assesment.web.dto.ReservationDto;
import com.assesment.web.dto.TableDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/reservation")
public class ReservationController {
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private TableService tableService;

    @GetMapping("/create")
    public String showCreateReservationForm(Model model) {
        ReservationDto reservationDto = new ReservationDto();
        model.addAttribute("dto", reservationDto);

        return "reservation/create";
    }

    @PostMapping("/save")
    public String createReservation(@Valid @ModelAttribute("dto") ReservationDto reservationDto, BindingResult result, Model model) {
        List<TableDto> tableDtoList = tableService.findAllAvailableTablesByStatus();

        if (tableDtoList == null && tableDtoList.size() == 0) {
            model.addAttribute("dto", reservationDto);
            return "redirect:/reservation/create?failed";
        }

        if (result.hasErrors()) {
            model.addAttribute("dto", reservationDto);
            return "reservation/create";
        }

        PersistentReservationEntity persistentReservationEntity = reservationService.saveReservation(reservationDto);
        if (persistentReservationEntity != null) {
            return "redirect:/reservation/list";
        } else {
            return "redirect:/reservation/create?failed";
        }
    }

    @GetMapping("/list")
    public String getReservation(Model model) {
        List<ReservationDto> reservationDtoList = reservationService.findAllReservations();
        model.addAttribute("reservations", reservationDtoList);
        return "/reservation/list";
    }
}
