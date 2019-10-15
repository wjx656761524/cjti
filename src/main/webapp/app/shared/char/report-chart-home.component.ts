import {Component, Input, OnChanges, OnInit} from '@angular/core';
import {Chart} from 'angular-highcharts';

@Component({
    selector: 'cjtj-report-chart-home',
    templateUrl: './report-chart-home.component.html',
})
export class ReportChartHomeComponent implements OnInit, OnChanges {


    option = {
        exporting: {
            enabled: false //用来设置是否显示‘打印’,'导出'等
        },
        chart: {
            type: 'line',
            height: '260px',
        },
        credits: {
            enabled: false
        },
        title: {
            text: ''
        },
        subtitle: {
            text: ''
        },
        tooltip: {
            shared: true,
        },
        xAxis: {
            categories: [0+'时',1+'时',2+'时',3+'时',4+'时',5+'时',6+'时',7+'时',8+'时',9+'时',10+'时',11+'时',12+'时',13+'时',14+'时',15+'时',16+'时',17+'时',18+'时',19+'时',20+'时',21+'时',22+'时',23+'时'],
            labels: {
                rotation: -30,
                style: {
                    fontSize: '10px',
                }

            }
        },
        yAxis: {
            gridLineWidth: 1,
            title: {
                text: '',
                style: {
                    color: '#E84B00'
                }
            },
            labels: {
                formatter() {
                    return this.value;
                },
                style: {
                    color: '#2489c5'
                }
            },
            opposite: false,

        },
        legend: {
            backgroundColor: '#FFFFFF',
        },
        series: [{
            name: '',
            color: '#d54e21',
            type: 'spline',
            data: [],
            visible: true,
            tooltip: {pointFormat: '<span style="color:{point.color}">\u25CF</span> {series.name}: <b>{point.y} </b><br/>'}
        }, {
            name: '',
            color: '#dae500',
            type: 'spline',
            data: [],
            visible: true,
            tooltip: {pointFormat: '<span style="color:{point.color}">\u25CF</span> {series.name}: <b>{point.y} </b><br/>'}
        }]
    };

    chartHome = new Chart(this.option);

    @Input() reports: any[];
    @Input() chartHeight: any;

    @Input() rptdailyOne: any;
    @Input() rptdailyTwo: any;
    @Input() dataContrast: any;
    ngOnInit(): void {
        if (this.chartHeight) {
            this.option.chart.height = this.chartHeight;
        }
    }

    ngOnChanges() {
        this.reloadReport();
    }

    private reloadReport() {
        const name = [];
        name.push(this.dataContrast);
        this.option = Object.assign({}, this.option);
        const chargeOne = [];
        const adPvOne = [];
        const clickOne = [];
        const ecpmOne = [];
        const ctrOne = [];
        const ecpcOne = [];

        const chargeTwo = [];
        const adPvTwo = [];
        const clickTwo = [];
        const ecpmTwo = [];
        const ctrTwo = [];
        const ecpcTwo = [];
        for(let i= 0; i<this.rptdailyOne.length ;i++){
            chargeOne.push(this.rptdailyOne[i].report.charge);
            adPvOne.push(this.rptdailyOne[i].report.adPv);
            clickOne.push(this.rptdailyOne[i].report.click);
            ecpcOne.push(this.rptdailyOne[i].report.ecpc);
            ctrOne.push(this.rptdailyOne[i].report.ctr);
            ecpmOne.push(this.rptdailyOne[i].report.ecpm);
        }
        for(let i= 0; i<this.rptdailyTwo.length ;i++){
            chargeTwo.push(this.rptdailyTwo[i].report.charge);
            adPvTwo.push(this.rptdailyTwo[i].report.adPv);
            clickTwo.push(this.rptdailyTwo[i].report.click);
            ecpcTwo.push(this.rptdailyTwo[i].report.ecpc);
            ctrTwo.push(this.rptdailyTwo[i].report.ctr);
            ecpmTwo.push(this.rptdailyTwo[i].report.ecpm);
        }
        if(this.dataContrast === 'charge'){
            this.option.series[0].data = chargeOne;
            this.option.series[1].data = chargeTwo;
        }
        if(this.dataContrast === 'adPv'){
            this.option.series[0].data = adPvOne;
            this.option.series[1].data = adPvTwo;
        }
        if(this.dataContrast === 'click'){
            this.option.series[0].data = clickOne;
            this.option.series[1].data = clickTwo;
        }
        if(this.dataContrast === 'ecpc'){
            this.option.series[0].data = ecpcOne;
            this.option.series[1].data = ecpcTwo;
        }
        if(this.dataContrast === 'ctr'){
            this.option.series[0].data = ctrOne;
            this.option.series[1].data = ctrTwo;
        }
        if(this.dataContrast === 'ecpm'){
            this.option.series[0].data = ecpmOne;
            this.option.series[1].data = ecpmTwo;
        }
        this.option.series[0].name = this.rptdailyOne[0].report.logDate;
        this.option.series[1].name = this.rptdailyTwo[0].report.logDate;
        this.chartHome = new Chart(this.option);
    }


}
