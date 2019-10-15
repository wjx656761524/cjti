import { Routes } from '@angular/router';


import {UserRouteAccessService} from"app/shared/auth/user-route-access-service";
import {CreativeComponent} from "app/creative/creative.component";

const CAMPAIGN_ROUTES = [
    {
        path: '',
        component: CreativeComponent,
        data: {
            pageTitle: '创意列表'
        }
    },
];

export const creativeState: Routes = [{
    path: '',
    data: {
        authorities: ['ROLE_USER']
    },
    canActivate: [UserRouteAccessService],
    children: CAMPAIGN_ROUTES
}];
