import { Routes } from '@angular/router';

import { UserComponent } from './user.component';
import {UserRouteAccessService} from "app/shared/auth/user-route-access-service";

const USER_ROUTES = [
    {
        path: '',
        component: UserComponent,
        data: {
            pageTitle: '账户信息'
        }
    },
];

export const userState: Routes = [{
    path: '',
    data: {
        authorities: ['ROLE_USER']
    },
    canActivate: [UserRouteAccessService],
    children: USER_ROUTES
}];
