package com.gestionstock.controller;

import com.gestionstock.dto.MouvementStockDTO;
import com.gestionstock.entity.MouvementStock;
import com.gestionstock.service.MouvementStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mouvements")
@RequiredArgsConstructor
public class MouvementStockController {

    private final MouvementStockService mouvementStockService;

    @PostMapping
    public MouvementStock enregistrer(@RequestBody MouvementStockDTO dto) {
        return mouvementStockService.enregistrerMouvement(dto);
    }

    @GetMapping("/produit/{idProduit}")
    public List<MouvementStock> historique(@PathVariable Long idProduit) {
        return mouvementStockService.historiqueParProduit(idProduit);
    }

    @GetMapping("/recents")
    public List<MouvementStock> recents() {
        return mouvementStockService.mouvementsRecents();
    }
}
