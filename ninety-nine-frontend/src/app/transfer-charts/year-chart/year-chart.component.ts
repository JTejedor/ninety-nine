import { Component, OnInit } from '@angular/core';
import { GenericChartData, DataSize } from '../generic-chart/generic-chart.component';
import { YearTransferViewerService } from './service/year-transfer-viewer.service';
import { first } from 'rxjs/operators';

@Component({
  selector: 'app-year-chart',
  templateUrl: './year-chart.component.html',
  styleUrls: ['./year-chart.component.sass']
})
export class YearChartComponent implements OnInit {

  chartData: GenericChartData;

  constructor(private yearTransferViewer: YearTransferViewerService) {
    this.chartData = {
      title: "Transfer per year",
      description: "View transfer received per year",
      dataSizeLabel: ["Last Two Year","Last Three Year","Last Five Year"],
      view: [400,400],
      data: [],
      // options
      showXAxis:true,
      showYAxis:true,
      gradient:false,
      showXAxisLabel:true,
      xAxisLabel:"Years",
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
      this.yearTransferViewer.getYearDataLast2Year().pipe(first()).subscribe(
        dataOneMonth => {
          this.chartData.data = dataOneMonth
        }
      )
    }
    else if(size === DataSize.MEDIUM){
      this.yearTransferViewer.getYearDataLast3Year().pipe(first()).subscribe(
        dataThreeMonth => {
          this.chartData.data = dataThreeMonth
        }
      )
    }
    else if(size === DataSize.LARGE){
      this.yearTransferViewer.getYearDataLast5Year().pipe(first()).subscribe(
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
