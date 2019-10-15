import {Injectable} from '@angular/core';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {ReportChartModalComponent} from './report-chart-modal.component';

@Injectable()
export class ReportChartModalService {

    constructor(private modalService: NgbModal) {
    }

    /**
     * 打开趋势图模态窗
     * @param {any[]} reports
     */
    openChartModal(reports: any[]) {
        const modalRef = this.modalService.open(ReportChartModalComponent, {size: 'lg', windowClass: 'reportChartModal'});
        modalRef.componentInstance.setReports(reports);
    }
}
