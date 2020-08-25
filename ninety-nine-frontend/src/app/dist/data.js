//{
//  "id": {
//    "year": 2020,
//    "month": 8,
//    "day": 17
//  },
//  "count": 14699
//}
var DayId = /** @class */ (function () {
    function DayId() {
        var _this = this;
        this.toString = function () {
            return _this.year + "-" + _this.month + "-" + _this.day;
        };
    }
    return DayId;
}());
var MonthId = /** @class */ (function () {
    function MonthId() {
        var _this = this;
        this.toString = function () {
            return _this.year + "-" + _this.month;
        };
    }
    return MonthId;
}());
var YearId = /** @class */ (function () {
    function YearId() {
        var _this = this;
        this.toString = function () {
            return "" + _this.year;
        };
    }
    return YearId;
}());
