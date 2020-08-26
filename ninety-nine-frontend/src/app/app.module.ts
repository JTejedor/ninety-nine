import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { DayChartComponent } from './transfer-charts/day-chart/day-chart.component';
import { YearChartComponent } from './transfer-charts/year-chart/year-chart.component';
import { HttpClientModule } from '@angular/common/http';
import { NgxChartsModule } from '@swimlane/ngx-charts';
import {MatButtonToggleModule} from '@angular/material/button-toggle';
import {MatExpansionModule} from '@angular/material/expansion';
import {MatGridListModule} from '@angular/material/grid-list';
import { DayTransferViewerService } from './transfer-charts/day-chart/service/day-transfer-viewer.service';
import { GenericChartComponent } from './transfer-charts/generic-chart/generic-chart.component';
import { ChartHttpProviderService } from './transfer-charts/service/chart-http-provider.service';
import { YearTransferViewerService } from './transfer-charts/year-chart/service/year-transfer-viewer.service';
import { MonthChartComponent } from './transfer-charts/month-chart/month-chart.component';
import { MonthTransferViewerService } from './transfer-charts/month-chart/service/month-transfer-viewer.service';
import {MatDividerModule} from '@angular/material/divider';
import { GenericMonthHitComponent } from './month-hits/generic-month-hit/generic-month-hit.component';
import { GenericMonthHitService } from './month-hits/generic-month-hit.service';
import { MaxTransferHitComponent } from './month-hits/max-transfer-hit/max-transfer-hit.component';
import { MaxTransferHitService } from './month-hits/max-transfer-hit/max-transfer-hit.service';
import { MaxAmountHitComponent } from './month-hits/max-amount-hit/max-amount-hit.component'
import { MaxAmountHitService } from './month-hits/max-amount-hit/max-amount-hit.service'

@NgModule({
  declarations: [
    AppComponent,
    DayChartComponent,
    YearChartComponent,
    GenericChartComponent,
    MonthChartComponent,
    GenericMonthHitComponent,
    MaxTransferHitComponent,
    MaxAmountHitComponent
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
    MatDividerModule
  ],
  providers: [
    DayTransferViewerService,
    ChartHttpProviderService,
    YearTransferViewerService,
    MonthTransferViewerService,
    GenericMonthHitService,
    MaxTransferHitService,
    MaxAmountHitService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
