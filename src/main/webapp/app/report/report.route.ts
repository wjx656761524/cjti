import { Routes } from '@angular/router';

import { ReportComponent } from './report.component';
import {UserRouteAccessService} from "app/shared/auth/user-route-access-service";
import {LocationReportComponent} from "app/report/location/location-Report.component";
import {CreativeReportComponent} from "app/report/creative/creative-Report.component";
import {CrowdReportComponent} from "app/report/crowd/crowd-Report.component";
import {AdGroupReportComponent} from "app/report/adGroup/adGroup-Report.component";

const RPORT_ROUTES = [
    {
        path: '',
        component: ReportComponent,
        data: {
            pageTitle: '账户信息'
        }
    },
    {
        path: 'adGroup',
        component: AdGroupReportComponent,
        data: {
            pageTitle: '单元信息'
        }
    },
    {
        path: 'location',
        component: LocationReportComponent ,
        data: {
            pageTitle: '资源位信息'
        }
    },
    {
        path: 'creative',
        component: CreativeReportComponent ,
        data: {
            pageTitle: '创意信息'
        }
    },
    {
        path: 'campai',
        component: ReportComponent,
        data: {
            pageTitle: '计划信息'
        }
    },
    {
        path: 'crowd',
        component: CrowdReportComponent,
        data: {
            pageTitle: '定向信息'
        }
    },
];

export const reportState: Routes = [{
    path: '',
    data: {
        authorities: ['ROLE_USER']
    },
    canActivate: [UserRouteAccessService],
    children: RPORT_ROUTES
}];
