import { Component, OnInit, OnDestroy } from '@angular/core';
import { DayTransferViewerService } from 'src/app/services/transfer/day/day-transfer-viewer.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-day-chart',
  templateUrl: './day-chart.component.html',
  styleUrls: ['./day-chart.component.sass']
})
export class DayChartComponent implements OnInit, OnDestroy {

  view: any[] = [700, 400];
  data: any[] = [];
  subscription: Subscription;
  // options
  showXAxis = true;
  showYAxis = true;
  gradient = false;
  showLegend = false;
  showXAxisLabel = true;
  xAxisLabel = 'Days';
  showYAxisLabel = true;
  yAxisLabel = 'Number of transfers';

  colorScheme = {
    domain: ['#5AA454']
  };

  constructor(public dayTransferViewer: DayTransferViewerService) { 
  }
  ngOnDestroy(): void {
    if(this.subscription){
      this.subscription.unsubscribe();
    }
  }

  ngOnInit() {
    this.subscription = this.dayTransferViewer.getDayDataMonth().subscribe(
      data =>
      this.data = data
    )
  }

}
