import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {Component, Input} from '@angular/core';

@Component({
    selector: 'cjtj-report-chart-modal',
    templateUrl: './report-chart-modal.component.html'
})
export class ReportChartModalComponent {

    @Input() reports;

    constructor(public activeModal: NgbActiveModal) {
    }

    public setReports(reports: any[]) {
        this.reports = reports;
    }
}
