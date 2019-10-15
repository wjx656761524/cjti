import {Component, Input, OnChanges, OnInit} from '@angular/core';
import {Chart} from 'angular-highcharts';

@Component({
    selector: 'cjtj-report-chart',
    templateUrl: './report-chart.component.html',
})
export class ReportChartComponent implements OnInit, OnChanges {


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
            categories: [],
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
            name: '消耗',
            color: '#d54e21',
            type: 'spline',
            data: [],
            visible: true,
            tooltip: {pointFormat: '<span style="color:{point.color}">\u25CF</span> {series.name}: <b>{point.y} </b><br/>'}
        }, {
            name: '展现量',
            color: '#dae500',
            type: 'spline',
            data: [],
            visible: true,
            tooltip: {pointFormat: '<span style="color:{point.color}">\u25CF</span> {series.name}: <b>{point.y} </b><br/>'}
        }, {
            name: '点击量',
            color: '#2489c5',
            type: 'spline',
            data: [],
            visible: true,
            tooltip: {pointFormat: '<span style="color:{point.color}">\u25CF</span> {series.name}: <b>{point.y} </b><br/>'}
        }, {
            name: '点击成本',
            color: '#9cc2cb',
            type: 'spline',
            data: [],
            visible: true,
            tooltip: {pointFormat: '<span style="color:{point.color}">\u25CF</span> {series.name}: <b>{point.y} </b><br/>'}
        }, {
            name: '点击率',
            color: '#73716e',
            type: 'spline',
            data: [],
            visible: true,
            tooltip: {pointFormat: '<span style="color:{point.color}">\u25CF</span> {series.name}: <b>{point.y} </b>%<br/>'}
        }, {
            name: '千次展现成本',
            color: '#f70',
            type: 'spline',
            data: [],
            visible: true,
            tooltip: {pointFormat: '<span style="color:{point.color}">\u25CF</span> {series.name}: <b>{point.y} </b>元<br/>'}
        }]
    };

    chart = new Chart(this.option);

    @Input() reports: any[];
    @Input() chartHeight: any;
    @Input() rptdaily: any;
    ngOnInit(): void {
        if (this.chartHeight) {
            this.option.chart.height = this.chartHeight;
        }
    }

    ngOnChanges() {
        this.reloadReport();
    }

    private reloadReport() {
        this.option = Object.assign({}, this.option);
        const data = [];
        const charge = [];
        const adPv = [];
        const click = [];
        const ecpm = [];
        const ctr = [];
        const ecpc = [];
        for(let i= 0; i<this.rptdaily.length ;i++){
            data.push(this.rptdaily[i].report.logDate);
            charge.push(this.rptdaily[i].report.charge);
            adPv.push(this.rptdaily[i].report.adPv);
            click.push(this.rptdaily[i].report.click);
            ecpc.push(this.rptdaily[i].report.ecpc);
            ctr.push(this.rptdaily[i].report.ctr);
            ecpm.push(this.rptdaily[i].report.ecpm);
        }
        this.option.xAxis.categories = data;
        this.option.series[0].data = charge;
        this.option.series[1].data = adPv;
        this.option.series[2].data = click;
        this.option.series[3].data = ecpc;
        this.option.series[4].data = ctr;
        this.option.series[5].data = ecpm;
        this.chart = new Chart(this.option);
    }

}
