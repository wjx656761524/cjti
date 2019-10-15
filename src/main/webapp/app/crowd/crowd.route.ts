import { Routes } from '@angular/router';

import { CrowdComponent } from './crowd.component';
import {UserRouteAccessService} from "app/shared/auth/user-route-access-service";

const CAMPAIGN_ROUTES = [
    {
        path: '',
        component: CrowdComponent,
        data: {
            pageTitle: '定向列表'
        }
    },
];

export const crowdState: Routes = [{
    path: '',
    data: {
        authorities: ['ROLE_USER']
    },
    canActivate: [UserRouteAccessService],
    children: CAMPAIGN_ROUTES
}];
