import { Component, OnInit, OnDestroy } from '@angular/core';
import { DayTransferViewerService } from 'src/app/services/transfer/day/day-transfer-viewer.service';
import { Subscription } from 'rxjs';
import { CountView } from 'src/app/data';
import { first } from 'rxjs/operators';

@Component({
  selector: 'app-day-chart',
  templateUrl: './day-chart.component.html',
  styleUrls: ['./day-chart.component.sass']
})
export class DayChartComponent implements OnInit {

  lastMonth: "last-month" = "last-month";
  lastThreeMonth: "last-three-month" = "last-three-month";
  lastSixMonth: "last-six-month" = "last-six-month";

  view: any[] = [700, 400];
  data: CountView[];
  dataOneMonth: CountView[] = [];
  dataThreeMonth: CountView[] = [];
  dataSixMonth: CountView[] = [];
  // options
  showXAxis = true;
  showYAxis = true;
  gradient = false;
  showXAxisLabel = true;
  xAxisLabel = 'Days';
  showYAxisLabel = true;
  yAxisLabel = 'Number of transfers';

  colorScheme = {
    domain: ['#4E18C5']
  };

  constructor(public dayTransferViewer: DayTransferViewerService) {
  }
  

  ngOnInit() {
    this.changePeriod(this.lastMonth)
  }

  onSelect(event) {

  }

  changePeriod(period:string){
    if(period ===this.lastMonth){
      this.dayTransferViewer.getDayDataLastMonth().pipe(first()).subscribe(
        dataOneMonth => {
          this.data = dataOneMonth
          this.showXAxisLabel = true
        }
      )
    }
    else if(period===this.lastThreeMonth){
      this.dayTransferViewer.getDayDataLastThreeMonth().pipe(first()).subscribe(
        dataThreeMonth => {
          this.data = dataThreeMonth
          this.showXAxisLabel = false
        }
      )
    }
    else if(period==this.lastSixMonth){
      this.dayTransferViewer.getDayDataLastSixMonth().pipe(first()).subscribe(
        dataSixMonth => {
          this.data = dataSixMonth
          this.showXAxisLabel = false
        }
      )
    }
  }

  periodChange(event){
    this.changePeriod(event.value)
  }

}
