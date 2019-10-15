import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import {CjtjSharedLibsModule} from "app/shared/shared-libs.module";
import {CjtjSharedCommonModule} from "app/shared/shared-common.module";
import {HttpClientModule} from "@angular/common/http";
import {CjtjLoginModalComponent} from "app/shared/login/login.component";
import {HasAnyAuthorityDirective} from "app/shared/auth/has-any-authority.directive";
import {ReportDateSelectComponent} from "app/shared/component/report-date-select.component";
import {NgbDateAdapter} from "@ng-bootstrap/ng-bootstrap";
import {AccountService} from "app/shared/auth/account.service";
import {NgbDateMomentAdapter} from "app/shared/util/datepicker-adapter";
import {StateStorageService} from "app/shared/auth/state-storage.service";
import {AuthServerProvider} from "app/shared/auth/auth-session.service";
import {CustomDatepickerI18n} from "app/shared/component/custom-datepicker-i18n.service";
import {LoginService} from "app/shared/login/login.service";
import {Principal} from "app/shared/auth/principal.service";
import {CjtjSortPipe} from "app/shared/component/CjtjSort.pipe";
import {YZFPipePipe} from "app/shared/component/YZFPipe.pipe";
import {FZYPipePipe} from "app/shared/component/FZYPipe.pipe";
import {ReportColumnsSelectComponent} from "app/shared/component/report-columns-select.component";
import {ReportChartComponent} from "app/shared/char/report-chart.component";
import {ReportChartModalComponent} from "app/shared/char/report-chart-modal.component";
import {ReportChartModalService} from "app/shared/char/report-chart-modal.service";
import {ChartModule} from "angular-highcharts";
import {PaginationComponent} from "app/shared/pagination/pagination.component";
import {ConfirmationPopoverModule} from "app/shared/modal";
import {CjtjNotifyService} from "app/shared/notify/notify.service";
import {ConfirmationPopoverOptions} from "app/shared/modal/confirmation-popover-options.provider";
import {Positioning} from "positioning";
import {CjtjNotifyComponent} from "app/shared/notify/notify.component";
import {LocationService} from "app/shared/location/location.service";
import {ReportChartHomeComponent} from "app/shared/char/report-chart-home.component";
import {CreativeService} from "app/shared/creative/creative.service";
import {CampaignService} from "app/shared/campaign/campaign.service";
import {SlideToggleComponent} from "app/shared/toggle/slide-toggle.component";
import {ItemService} from "app/shared/Item/item.service";
import {AddCampaignComponent} from "app/campaign/add/add-campaign.component";
import {CrowdReportService} from "app/shared/crowd/crowd.service";
import {CrowdReportComponent} from "app/report/crowd/crowd-Report.component";
import {AdgroupService} from "app/shared/adgroup/adgroup.service";
import {ReportService} from "app/shared/report/report.service";

@NgModule({
    imports: [
        CjtjSharedLibsModule,
        CjtjSharedCommonModule,
        HttpClientModule,
        ChartModule
    ],
    declarations: [
        CjtjLoginModalComponent,
        HasAnyAuthorityDirective,
        ReportDateSelectComponent,
        CjtjNotifyComponent,
        CjtjSortPipe,
        YZFPipePipe,
        FZYPipePipe,
        ReportColumnsSelectComponent,
        ReportChartComponent,
        ReportChartHomeComponent,
        ReportChartModalComponent,
        PaginationComponent,
        SlideToggleComponent,
        AddCampaignComponent,




    ],
    providers: [
        { provide: NgbDateAdapter, useClass: NgbDateMomentAdapter },
        AccountService,
        StateStorageService,
        LoginService,
        AuthServerProvider,
        CustomDatepickerI18n,
        CjtjSortPipe,
        YZFPipePipe,
        FZYPipePipe,
        ReportChartModalService,
        CjtjNotifyService,
        ConfirmationPopoverOptions,
        Positioning,

        LocationService,
        CreativeService,
        ItemService,
        CampaignService,
        CrowdReportService,
        CreativeService,
        AdgroupService,
        ReportService,

    ],
    entryComponents: [CjtjLoginModalComponent,ReportChartModalComponent,CjtjNotifyComponent,AddCampaignComponent],
    exports: [
        CjtjSharedCommonModule,
        CjtjLoginModalComponent,
        HasAnyAuthorityDirective,
        CjtjSortPipe,
        YZFPipePipe,
        FZYPipePipe,
        ReportColumnsSelectComponent,
        ReportChartComponent,
        ReportChartHomeComponent,
        ReportChartModalComponent,
        PaginationComponent,
        ReportDateSelectComponent,
        ConfirmationPopoverModule,
        CjtjNotifyComponent,
        SlideToggleComponent,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CjtjSharedModule {
    static forRoot() {
        return {
            ngModule: CjtjSharedModule,
            providers: [Principal]
        };
    }
}
