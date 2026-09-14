import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ChartConfiguration, ChartData } from 'chart.js';
import { NgChartsModule } from 'ng2-charts';
import { Kpi } from '../models/kpi.model';
import { DashboardService } from './dashboard.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, NgChartsModule],
  templateUrl: './dashboard.component.html',
})
export class DashboardComponent implements OnInit {
  kpi?: Kpi;
  chargement = true;

  // Graphique 1 : evolution des mouvements (courbes entrees/sorties)
  evolutionChartType: ChartConfiguration['type'] = 'line';
  evolutionChartData: ChartData<'line'> = {
    labels: [],
    datasets: [
      { data: [], label: 'Entrées', borderColor: '#0f6e56', backgroundColor: 'rgba(15,110,86,0.1)', tension: 0.3 },
      { data: [], label: 'Sorties', borderColor: '#993c1d', backgroundColor: 'rgba(153,60,29,0.1)', tension: 0.3 },
    ],
  };
  evolutionChartOptions: ChartConfiguration['options'] = {
    responsive: true,
    plugins: { legend: { position: 'top' } },
    scales: { y: { beginAtZero: true, ticks: { precision: 0 } } },
  };

  // Graphique 2 : repartition des produits par categorie (camembert)
  repartitionChartType: ChartConfiguration['type'] = 'doughnut';
  repartitionChartData: ChartData<'doughnut'> = {
    labels: [],
    datasets: [
      {
        data: [],
        backgroundColor: ['#0f6e56', '#534ab7', '#993c1d', '#b5750f', '#185fa5', '#7a7a72'],
      },
    ],
  };
  repartitionChartOptions: ChartConfiguration['options'] = {
    responsive: true,
    plugins: { legend: { position: 'right' } },
  };

  constructor(private dashboardService: DashboardService) {}

  ngOnInit(): void {
    this.dashboardService.getKpi().subscribe({
      next: (data) => {
        this.kpi = data;
        this.chargement = false;
      },
      error: () => (this.chargement = false),
    });

    this.dashboardService.getEvolutionMouvements(14).subscribe((data) => {
      this.evolutionChartData = {
        labels: data.map((d) => this.formaterDate(d.date)),
        datasets: [
          { ...this.evolutionChartData.datasets[0], data: data.map((d) => d.entrees) },
          { ...this.evolutionChartData.datasets[1], data: data.map((d) => d.sorties) },
        ],
      };
    });

    this.dashboardService.getRepartitionCategories().subscribe((data) => {
      this.repartitionChartData = {
        labels: data.map((d) => d.nomCategorie),
        datasets: [{ ...this.repartitionChartData.datasets[0], data: data.map((d) => d.nombreProduits) }],
      };
    });
  }

  private formaterDate(dateIso: string): string {
    const [, mois, jour] = dateIso.split('-');
    return `${jour}/${mois}`;
  }
}