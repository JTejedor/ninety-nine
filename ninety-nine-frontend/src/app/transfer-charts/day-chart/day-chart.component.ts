import { Component, OnInit } from '@angular/core';
import { DayTransferViewerService } from './service/day-transfer-viewer.service';
import { CountView } from 'src/app/data';
import { first } from 'rxjs/operators';
import { GenericChartData, DataSize } from '../generic-chart/generic-chart.component';

@Component({
  selector: 'app-day-chart',
  templateUrl: './day-chart.component.html',
  styleUrls: ['./day-chart.component.sass']
})
export class DayChartComponent implements OnInit {

  chartData: GenericChartData;

  constructor(private dayTransferViewer: DayTransferViewerService) {
    this.chartData = {
      title: "Transfer per day",
      description: "View transfer received per day",
      dataSizeLabel: ["Last Month","Last three Months","Last Six Months"],
      view: [1200,400],
      data: [],
      // options
      showXAxis:true,
      showYAxis:true,
      gradient:false,
      showXAxisLabel:true,
      xAxisLabel:"Days",
      showYAxisLabel:true,
      yAxisLabel:"Transfers",
      colorScheme: {
        domain: ['#4E18C5']
      }
    }
  }

  ngOnInit() {
    this.changePeriod(DataSize.SMALL)
  }

  changePeriod(size: DataSize){
    if(size === DataSize.SMALL){
      this.dayTransferViewer.getDayDataLastMonth().pipe(first()).subscribe(
        dataOneMonth => {
          this.chartData.data = dataOneMonth
        }
      )
    }
    else if(size === DataSize.MEDIUM){
      this.dayTransferViewer.getDayDataLastThreeMonth().pipe(first()).subscribe(
        dataThreeMonth => {
          this.chartData.data = dataThreeMonth
        }
      )
    }
    else if(size === DataSize.LARGE){
      this.dayTransferViewer.getDayDataLastSixMonth().pipe(first()).subscribe(
        dataSixMonth => {
          this.chartData.data = dataSixMonth
        }
      )
    }
  }

  periodChange(event){
    this.changePeriod(DataSize[event.value as string])
  }

}
