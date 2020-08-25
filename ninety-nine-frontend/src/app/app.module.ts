import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { DayChartComponent } from './transfer-charts/day-chart/day-chart.component';
import { MonthChartComponent } from './transfer-charts/month-chart/month-chart.component';
import { YearChartComponent } from './transfer-charts/year-chart/year-chart.component';
import { HttpClientModule } from '@angular/common/http';
import { TransferPerDayService } from './services/transfer/day/transfer-per-day.service';
import { TransferPerMonthService } from './services/transfer/transfer-per-month.service';
import { TransferPerYearService } from './services/transfer/transfer-per-year.service';
import { DataToViewConvertService } from './services/data-to-view-convert.service';
import { NgxChartsModule } from '@swimlane/ngx-charts';
import { DayTransferViewerService } from './services/transfer/day/day-transfer-viewer.service';

@NgModule({
  declarations: [
    AppComponent,
    DayChartComponent,
    MonthChartComponent,
    YearChartComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NoopAnimationsModule,
    HttpClientModule,
    NgxChartsModule,
  ],
  providers: [
    TransferPerDayService,
    TransferPerMonthService,
    TransferPerYearService,
    DataToViewConvertService,
    DayTransferViewerService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
