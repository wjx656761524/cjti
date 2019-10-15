import { Route } from '@angular/router';
import { SettingsComponent } from './settings.component';
import {UserRouteAccessService} from "app/shared/auth/user-route-access-service";

export const settingsRoute: Route = {
    path: 'settings',
    component: SettingsComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: '设置'
    },
    canActivate: [UserRouteAccessService]
};
