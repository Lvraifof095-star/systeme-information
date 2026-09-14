package com.gestionstock.controller;

import com.gestionstock.entity.Alerte;
import com.gestionstock.service.AlerteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alertes")
@RequiredArgsConstructor
public class AlerteController {

    private final AlerteService alerteService;

    @GetMapping
    public List<Alerte> lister() {
        return alerteService.listerNonTraitees();
    }

    @PutMapping("/{id}/traiter")
    public Alerte traiter(@PathVariable Long id) {
        return alerteService.traiter(id);
    }
}
