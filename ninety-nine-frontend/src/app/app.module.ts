import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { DayChartComponent } from './transfer-charts/day-chart/day-chart.component';
import { YearChartComponent } from './transfer-charts/year-chart/year-chart.component';
import { HttpClientModule } from '@angular/common/http';
import { NgxChartsModule } from '@swimlane/ngx-charts';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatGridListModule } from '@angular/material/grid-list';
import { DayTransferViewerService } from './transfer-charts/day-chart/service/day-transfer-viewer.service';
import { GenericChartComponent } from './transfer-charts/generic-chart/generic-chart.component';
import { ChartHttpProviderService } from './transfer-charts/service/chart-http-provider.service';
import { YearTransferViewerService } from './transfer-charts/year-chart/service/year-transfer-viewer.service';
import { MonthChartComponent } from './transfer-charts/month-chart/month-chart.component';
import { MonthTransferViewerService } from './transfer-charts/month-chart/service/month-transfer-viewer.service';
import { MatDividerModule } from '@angular/material/divider';
import { GenericMonthHitComponent } from './month-hits/generic-month-hit/generic-month-hit.component';
import { GenericMonthHitService } from './month-hits/generic-month-hit.service';
import { MaxTransferHitComponent } from './month-hits/max-transfer-hit/max-transfer-hit.component';
import { MaxTransferHitService } from './month-hits/max-transfer-hit/max-transfer-hit.service';
import { MaxAmountHitComponent } from './month-hits/max-amount-hit/max-amount-hit.component'
import { MaxAmountHitService } from './month-hits/max-amount-hit/max-amount-hit.service';
import { MatSelectModule } from '@angular/material/select';
import { IbanSelectorComponent } from './iban-transfer/iban-selector/iban-selector.component'
import { IbanSelectorService } from './iban-transfer/iban-selector/iban-selector.service';
import { IbanLastTransfersViewComponent } from './iban-transfer/iban-last-transfers-view/iban-last-transfers-view.component'
import { MatTableModule } from '@angular/material/table'
import { IbanLastTransfersViewService } from './iban-transfer/iban-last-transfers-view/iban-last-transfers-view.service'

@NgModule({
  declarations: [
    AppComponent,
    DayChartComponent,
    YearChartComponent,
    GenericChartComponent,
    MonthChartComponent,
    GenericMonthHitComponent,
    MaxTransferHitComponent,
    MaxAmountHitComponent,
    IbanSelectorComponent,
    IbanLastTransfersViewComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NoopAnimationsModule,
    HttpClientModule,
    NgxChartsModule,
    MatButtonToggleModule,
    MatExpansionModule,
    MatGridListModule,
    MatDividerModule,
    MatSelectModule,
    MatTableModule
  ],
  providers: [
    DayTransferViewerService,
    ChartHttpProviderService,
    YearTransferViewerService,
    MonthTransferViewerService,
    GenericMonthHitService,
    MaxTransferHitService,
    MaxAmountHitService,
    IbanSelectorService,
    IbanLastTransfersViewService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
