package com.assesment.web.controller;

import com.assesment.service.TableService;
import com.assesment.web.dto.TableDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller()
@RequestMapping("/table")
public class TableController {
    @Autowired
    private TableService tableService;

    @GetMapping("/create")
    public String showCreateTableForm(Model model) {
        TableDto tableDto = new TableDto();
        model.addAttribute("dto", tableDto);

        return "table/create";
    }

    @PostMapping("/save")
    public String createTable(@Valid @ModelAttribute("dto") TableDto tableDto, BindingResult result, Model model) {
        TableDto existingTable = tableService.findTableByName(tableDto.getName());

        if (existingTable != null && existingTable.getName() != null && !existingTable.getName().isEmpty()) {
            result.rejectValue("name", null, "There is already table created with the same name");
        }

        if (result.hasErrors()) {
            model.addAttribute("dto", tableDto);
            return "table/create";
        }

        tableService.saveTable(tableDto);
        return "redirect:/table/list";
    }

    @GetMapping("/list")
    public String getTables(Model model) {
        List<TableDto> tableDtoList = tableService.findAllTables();
        model.addAttribute("tables", tableDtoList);
        return "/table/list";
    }

}
