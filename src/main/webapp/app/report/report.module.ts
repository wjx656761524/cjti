import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import {
    ReportComponent, reportState,
} from './';
import {CjtjSharedModule} from "app/shared";
import {AdGroupReportComponent} from "app/report/adGroup/adGroup-Report.component";
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {LocationReportComponent} from "app/report/location/location-Report.component";
import {CreativeReportComponent} from "app/report/creative/creative-Report.component";
import {ReportService} from "app/shared/report/report.service";
import {CrowdReportComponent} from "app/report/crowd/crowd-Report.component";

@NgModule({
    imports: [
        RouterModule.forChild(reportState),
        CjtjSharedModule
    ],
    exports: [
        RouterModule
    ],
    declarations: [
        ReportComponent,
        AdGroupReportComponent,
        LocationReportComponent,
        CreativeReportComponent,
        CrowdReportComponent
    ],
    providers: [
            NgbActiveModal,
            ReportService
    ],
    entryComponents: [AdGroupReportComponent,LocationReportComponent,CreativeReportComponent,ReportComponent,CrowdReportComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CjtjReportModule {}
