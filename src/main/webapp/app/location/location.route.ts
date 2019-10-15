import { Routes } from '@angular/router';

import {LocationComponent} from "app/location/location.component";
import {UserRouteAccessService} from"app/shared/auth/user-route-access-service";

const CAMPAIGN_ROUTES = [
    {
        path: '',
        component: LocationComponent,
        data: {
            pageTitle: '资源位列表'
        }
    },

];

export const locationState: Routes = [{
    path: '',
    data: {
        authorities: ['ROLE_USER']
    },
    canActivate: [UserRouteAccessService],
    children: CAMPAIGN_ROUTES
}];
