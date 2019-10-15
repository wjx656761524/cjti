import { Routes } from '@angular/router';
import {GroupComponent} from './group.component';
import {UserRouteAccessService} from "app/shared/auth/user-route-access-service";
import {AddMainGroupComponent} from "app/group/add/add-mainGroup.component";
import {AddSecondMainGroupComponent} from "app/group/add/addSecond/addSecond-mainGroup.component";

const GROUP_ROUTES = [
    {
        path: '',
        component: GroupComponent,
        data: {
            pageTitle: '单元列表'
        }
    },
    {
        path: 'addGroup',
        component: AddMainGroupComponent,
        data: {
            pageTitle: '推广新单元'
        }
    }

];

export const groupState: Routes = [{
    path: '',
    data: {
        authorities: ['ROLE_USER']
    },
    canActivate: [UserRouteAccessService],
    children: GROUP_ROUTES
}];
