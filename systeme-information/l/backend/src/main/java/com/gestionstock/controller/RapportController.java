package com.gestionstock.controller;

import com.gestionstock.service.RapportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/rapports")
@RequiredArgsConstructor
public class RapportController {

    private static final MediaType EXCEL_TYPE =
            MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

    private final RapportService rapportService;

    // ---------- Produits ----------
    @GetMapping("/produits/excel")
    public ResponseEntity<byte[]> produitsExcel() {
        byte[] fichier = rapportService.genererExcel(
                "Produits", rapportService.entetesProduits(), rapportService.lignesProduits());
        return reponseFichier(fichier, EXCEL_TYPE, "rapport_produits.xlsx");
    }

    @GetMapping("/produits/pdf")
    public ResponseEntity<byte[]> produitsPdf() {
        byte[] fichier = rapportService.genererPdf(
                "Rapport des produits", rapportService.entetesProduits(), rapportService.lignesProduits());
        return reponseFichier(fichier, MediaType.APPLICATION_PDF, "rapport_produits.pdf");
    }

    // ---------- Mouvements ----------
    @GetMapping("/mouvements/excel")
    public ResponseEntity<byte[]> mouvementsExcel(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        byte[] fichier = rapportService.genererExcel(
                "Mouvements", rapportService.entetesMouvements(), rapportService.lignesMouvements(debut, fin));
        return reponseFichier(fichier, EXCEL_TYPE, "rapport_mouvements.xlsx");
    }

    @GetMapping("/mouvements/pdf")
    public ResponseEntity<byte[]> mouvementsPdf(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        byte[] fichier = rapportService.genererPdf(
                "Rapport des mouvements", rapportService.entetesMouvements(), rapportService.lignesMouvements(debut, fin));
        return reponseFichier(fichier, MediaType.APPLICATION_PDF, "rapport_mouvements.pdf");
    }

    // ---------- Fournisseurs ----------
    @GetMapping("/fournisseurs/excel")
    public ResponseEntity<byte[]> fournisseursExcel() {
        byte[] fichier = rapportService.genererExcel(
                "Fournisseurs", rapportService.entetesFournisseurs(), rapportService.lignesFournisseurs());
        return reponseFichier(fichier, EXCEL_TYPE, "rapport_fournisseurs.xlsx");
    }

    @GetMapping("/fournisseurs/pdf")
    public ResponseEntity<byte[]> fournisseursPdf() {
        byte[] fichier = rapportService.genererPdf(
                "Rapport des fournisseurs", rapportService.entetesFournisseurs(), rapportService.lignesFournisseurs());
        return reponseFichier(fichier, MediaType.APPLICATION_PDF, "rapport_fournisseurs.pdf");
    }

    // ---------- Stock ----------
    @GetMapping("/stock/excel")
    public ResponseEntity<byte[]> stockExcel() {
        byte[] fichier = rapportService.genererExcel(
                "Stock", rapportService.entetesStock(), rapportService.lignesStock());
        return reponseFichier(fichier, EXCEL_TYPE, "rapport_stock.xlsx");
    }

    @GetMapping("/stock/pdf")
    public ResponseEntity<byte[]> stockPdf() {
        byte[] fichier = rapportService.genererPdf(
                "Rapport des stocks", rapportService.entetesStock(), rapportService.lignesStock());
        return reponseFichier(fichier, MediaType.APPLICATION_PDF, "rapport_stock.pdf");
    }

    // ---------- Aide ----------
    private ResponseEntity<byte[]> reponseFichier(byte[] contenu, MediaType type, String nomFichier) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition.attachment().filename(nomFichier).build());
        return ResponseEntity.ok().headers(headers).contentType(type).body(contenu);
    }
}