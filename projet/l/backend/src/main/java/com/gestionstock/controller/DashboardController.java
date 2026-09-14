package com.gestionstock.controller;

import com.gestionstock.dto.EvolutionMouvementDTO;
import com.gestionstock.dto.KpiDTO;
import com.gestionstock.dto.RepartitionCategorieDTO;
import com.gestionstock.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/kpi")
    public KpiDTO getKpi() {
        return dashboardService.calculerKpi();
    }

    @GetMapping("/evolution-mouvements")
    public List<EvolutionMouvementDTO> getEvolutionMouvements(@RequestParam(defaultValue = "14") int jours) {
        return dashboardService.calculerEvolutionMouvements(jours);
    }

    @GetMapping("/repartition-categories")
    public List<RepartitionCategorieDTO> getRepartitionCategories() {
        return dashboardService.calculerRepartitionCategories();
    }
}
