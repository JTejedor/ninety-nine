"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
exports.__esModule = true;
exports.AppModule = void 0;
var platform_browser_1 = require("@angular/platform-browser");
var core_1 = require("@angular/core");
var app_routing_module_1 = require("./app-routing.module");
var app_component_1 = require("./app.component");
var animations_1 = require("@angular/platform-browser/animations");
var day_chart_component_1 = require("./transfer-charts/day-chart/day-chart.component");
var month_chart_component_1 = require("./transfer-charts/month-chart/month-chart.component");
var year_chart_component_1 = require("./transfer-charts/year-chart/year-chart.component");
var http_1 = require("@angular/common/http");
var transfer_per_day_service_1 = require("./services/transfer/day/transfer-per-day.service");
var transfer_per_month_service_1 = require("./services/transfer/transfer-per-month.service");
var transfer_per_year_service_1 = require("./services/transfer/transfer-per-year.service");
var data_to_view_convert_service_1 = require("./services/data-to-view-convert.service");
var AppModule = /** @class */ (function () {
    function AppModule() {
    }
    AppModule = __decorate([
        core_1.NgModule({
            declarations: [
                app_component_1.AppComponent,
                day_chart_component_1.DayChartComponent,
                month_chart_component_1.MonthChartComponent,
                year_chart_component_1.YearChartComponent
            ],
            imports: [
                platform_browser_1.BrowserModule,
                app_routing_module_1.AppRoutingModule,
                animations_1.NoopAnimationsModule,
                http_1.HttpClientModule,
            ],
            providers: [
                transfer_per_day_service_1.TransferPerDayService,
                transfer_per_month_service_1.TransferPerMonthService,
                transfer_per_year_service_1.TransferPerYearService,
                data_to_view_convert_service_1.DataToViewConvertService
            ],
            bootstrap: [app_component_1.AppComponent]
        })
    ], AppModule);
    return AppModule;
}());
exports.AppModule = AppModule;
