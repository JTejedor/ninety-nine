import { Component, OnInit } from '@angular/core';
import { MonthTransferViewerService } from './service/month-transfer-viewer.service';
import { GenericChartData, DataSize } from '../generic-chart/generic-chart.component';
import { first } from 'rxjs/operators';

@Component({
  selector: 'app-month-chart',
  templateUrl: './month-chart.component.html',
  styleUrls: ['./month-chart.component.sass']
})
export class MonthChartComponent implements OnInit {

  chartData: GenericChartData;

  constructor(private monthTransferViewer: MonthTransferViewerService) {
    this.chartData = {
      title: "Transfer per month",
      description: "View transfer received per month",
      dataSizeLabel: ["Last Year","Last Three Years","Last Five Years"],
      view: [800,400],
      data: [],
      // options
      showXAxis:true,
      showYAxis:true,
      gradient:false,
      showXAxisLabel:true,
      xAxisLabel:"Months",
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
      this.monthTransferViewer.getMonthDataLastYear().pipe(first()).subscribe(
        dataLastYear => {
          this.chartData.data = dataLastYear
        }
      )
    }
    else if(size === DataSize.MEDIUM){
      this.monthTransferViewer.getMonthDataLast3Year().pipe(first()).subscribe(
        dataLast3Year => {
          this.chartData.data = dataLast3Year
        }
      )
    }
    else if(size === DataSize.LARGE){
      this.monthTransferViewer.getMonthDataLast5Year().pipe(first()).subscribe(
        dataLast5Year => {
          this.chartData.data = dataLast5Year
        }
      )
    }
  }

  periodChange(event){
    this.changePeriod(DataSize[event.value as string])
  }

}
