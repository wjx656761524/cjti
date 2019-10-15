import { Routes } from '@angular/router';

import { CampaignComponent } from './campaign.component';
import {UserRouteAccessService} from "app/shared/auth/user-route-access-service";

const CAMPAIGN_ROUTES = [
    {
        path: '',
        component: CampaignComponent,
        data: {
            pageTitle: '计划列表'
        }
    },
    {
        path: 'addCampaign',
        component: CampaignComponent,
        data: {
            pageTitle: '新建计划'
        }
    },
];

export const campaignState: Routes = [{
    path: '',
    data: {
        authorities: ['ROLE_USER']
    },
    canActivate: [UserRouteAccessService],
    children: CAMPAIGN_ROUTES
}];
