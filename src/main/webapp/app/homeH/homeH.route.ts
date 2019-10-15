import {Route, Routes} from '@angular/router';

import { HomeHComponent } from './';
import {UserRouteAccessService} from "app/shared/auth/user-route-access-service";


export const HOMEH_ROUTE: Route = {
    path: '',
    component: HomeHComponent,
    data: {
        pageTitle: '历史投放数据'
    },
    canActivate: [UserRouteAccessService]
};



