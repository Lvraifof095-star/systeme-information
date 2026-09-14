package com.gestionstock.service;

import com.gestionstock.entity.Fournisseur;
import com.gestionstock.entity.MouvementStock;
import com.gestionstock.entity.Produit;
import com.gestionstock.repository.FournisseurRepository;
import com.gestionstock.repository.MouvementStockRepository;
import com.gestionstock.repository.ProduitRepository;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RapportService {

    private final ProduitRepository produitRepository;
    private final MouvementStockRepository mouvementRepository;
    private final FournisseurRepository fournisseurRepository;

    private static final DateTimeFormatter FORMAT_DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // =========================================================
    // Generation generique : Excel
    // =========================================================
    public byte[] genererExcel(String titre, String[] entetes, List<String[]> lignes) {
        try (Workbook classeur = new XSSFWorkbook(); ByteArrayOutputStream sortie = new ByteArrayOutputStream()) {
            Sheet feuille = classeur.createSheet(titre);

            CellStyle styleEntete = classeur.createCellStyle();
            Font policeEntete = classeur.createFont();
            policeEntete.setBold(true);
            policeEntete.setColor(IndexedColors.WHITE.getIndex());
            styleEntete.setFont(policeEntete);
            styleEntete.setFillForegroundColor(IndexedColors.DARK_TEAL.getIndex());
            styleEntete.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            Row ligneEntete = feuille.createRow(0);
            for (int i = 0; i < entetes.length; i++) {
                Cell cellule = ligneEntete.createCell(i);
                cellule.setCellValue(entetes[i]);
                cellule.setCellStyle(styleEntete);
            }

            for (int i = 0; i < lignes.size(); i++) {
                Row ligne = feuille.createRow(i + 1);
                String[] valeurs = lignes.get(i);
                for (int j = 0; j < valeurs.length; j++) {
                    ligne.createCell(j).setCellValue(valeurs[j]);
                }
            }

            for (int i = 0; i < entetes.length; i++) {
                feuille.autoSizeColumn(i);
            }

            classeur.write(sortie);
            return sortie.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la génération du fichier Excel", e);
        }
    }

    // =========================================================
    // Generation generique : PDF
    // =========================================================
    public byte[] genererPdf(String titre, String[] entetes, List<String[]> lignes) {
        try (ByteArrayOutputStream sortie = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4.rotate(), 30, 30, 40, 30);
            PdfWriter.getInstance(document, sortie);
            document.open();

            com.lowagie.text.Font policeTitre =
                    new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 16, com.lowagie.text.Font.BOLD);
            Paragraph paragrapheTitre = new Paragraph(titre, policeTitre);
            paragrapheTitre.setSpacingAfter(6);
            document.add(paragrapheTitre);

            com.lowagie.text.Font policeDate =
                    new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 9, com.lowagie.text.Font.ITALIC);
            Paragraph dateGeneration = new Paragraph(
                    "Généré le " + LocalDate.now().format(FORMAT_DATE), policeDate);
            dateGeneration.setSpacingAfter(14);
            document.add(dateGeneration);

            PdfPTable tableau = new PdfPTable(entetes.length);
            tableau.setWidthPercentage(100);

            com.lowagie.text.Font policeEntete = new com.lowagie.text.Font(
                    com.lowagie.text.Font.HELVETICA, 10, com.lowagie.text.Font.BOLD, java.awt.Color.WHITE);
            for (String entete : entetes) {
                PdfPCell cellule = new PdfPCell(new Phrase(entete, policeEntete));
                cellule.setBackgroundColor(new java.awt.Color(15, 110, 86));
                cellule.setPadding(6);
                tableau.addCell(cellule);
            }

            com.lowagie.text.Font policeCellule = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 9);
            for (String[] ligne : lignes) {
                for (String valeur : ligne) {
                    PdfPCell cellule = new PdfPCell(new Phrase(valeur, policeCellule));
                    cellule.setPadding(5);
                    tableau.addCell(cellule);
                }
            }

            document.add(tableau);
            document.close();
            return sortie.toByteArray();
        } catch (DocumentException | IOException e) {
            throw new RuntimeException("Erreur lors de la génération du fichier PDF", e);
        }
    }

    // =========================================================
    // Rapport des produits
    // =========================================================
    public String[] entetesProduits() {
        return new String[]{"Référence", "Désignation", "Catégorie", "Prix unitaire", "Quantité en stock", "Seuil d'alerte"};
    }

    public List<String[]> lignesProduits() {
        return produitRepository.findAll().stream().map(this::ligneProduit).collect(Collectors.toList());
    }

    private String[] ligneProduit(Produit p) {
        return new String[]{
                p.getReference(),
                p.getDesignation(),
                p.getCategorie() != null ? p.getCategorie().getNomCategorie() : "",
                String.format("%.2f", p.getPrixUnitaire()),
                p.getStock() != null ? String.valueOf(p.getStock().getQuantiteActuelle()) : "0",
                String.valueOf(p.getSeuilAlerte()),
        };
    }

    // =========================================================
    // Rapport des mouvements (sur une periode)
    // =========================================================
    public String[] entetesMouvements() {
        return new String[]{"Date", "Heure", "Type", "Nature", "Produit", "Quantité", "Utilisateur"};
    }

    public List<String[]> lignesMouvements(LocalDate debut, LocalDate fin) {
        return mouvementRepository.findAll().stream()
                .filter(m -> !m.getDate().isBefore(debut) && !m.getDate().isAfter(fin))
                .sorted((a, b) -> b.getDate().compareTo(a.getDate())) // plus recent en premier
                .map(this::ligneMouvement)
                .collect(Collectors.toList());
    }

    private String[] ligneMouvement(MouvementStock m) {
        return new String[]{
                m.getDate() != null ? m.getDate().format(FORMAT_DATE) : "",
                m.getHeure() != null ? m.getHeure().toString() : "",
                m.getTypeMouvement() == com.gestionstock.entity.TypeMouvement.ENTREE ? "Entrée" : "Sortie",
                m.getSousType(),
                m.getProduit() != null ? m.getProduit().getDesignation() : "",
                String.valueOf(m.getQuantite()),
                m.getUtilisateur() != null ? m.getUtilisateur().getNom() : "",
        };
    }

    // =========================================================
    // Rapport des fournisseurs
    // =========================================================
    public String[] entetesFournisseurs() {
        return new String[]{"Nom", "Contact", "Adresse"};
    }

    public List<String[]> lignesFournisseurs() {
        return fournisseurRepository.findAll().stream().map(this::ligneFournisseur).collect(Collectors.toList());
    }

    private String[] ligneFournisseur(Fournisseur f) {
        return new String[]{
                f.getNom(),
                f.getContact() != null ? f.getContact() : "",
                f.getAdresse() != null ? f.getAdresse() : "",
        };
    }

    // =========================================================
    // Rapport des stocks (valeur, alertes)
    // =========================================================
    public String[] entetesStock() {
        return new String[]{"Référence", "Désignation", "Quantité", "Prix unitaire", "Valeur du stock", "État"};
    }

    public List<String[]> lignesStock() {
        return produitRepository.findAll().stream().map(this::ligneStock).collect(Collectors.toList());
    }

    private String[] ligneStock(Produit p) {
        int quantite = p.getStock() != null ? p.getStock().getQuantiteActuelle() : 0;
        double valeur = quantite * p.getPrixUnitaire();
        String etat = quantite == 0 ? "Rupture" : (quantite <= p.getSeuilAlerte() ? "Stock faible" : "Normal");

        return new String[]{
                p.getReference(),
                p.getDesignation(),
                String.valueOf(quantite),
                String.format("%.2f", p.getPrixUnitaire()),
                String.format("%.2f", valeur),
                etat,
        };
    }
}