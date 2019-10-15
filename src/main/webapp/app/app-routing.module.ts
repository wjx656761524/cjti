import { NgModule } from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {ErrorComponent, errorRoute, NavbarComponent} from './layouts';
import { DEBUG_INFO_ENABLED } from 'app/app.constants';
import {AccessdeniedComponent} from "app/layouts/error/accessdenied.component";

const APP_ROUTES: Routes = [
    {
        path: '',
        component: NavbarComponent,
        outlet: 'navbar'
    },
    {
        path: 'error',
        component: ErrorComponent,
        data: {
            authorities: [],
            pageTitle: 'Error page!'
        },
    },
    {
        path: 'accessdenied',
        component: AccessdeniedComponent,
        data: {
            authorities: [],
            pageTitle: 'Error page!',
            error403: true
        },
    },
    {
        path: 'campaign',
        loadChildren: './campaign/campaign.module#CjtjCampaignModule'
    },
    {
        path: 'user',
        loadChildren: './user/user.module#CjtjUserModule'
    },
    {
        path: 'report',
        loadChildren: './report/report.module#CjtjReportModule'

    },

    {
        path: 'crowd',
        loadChildren: './crowd/crowd.module#CjtjCrowdModule',
    },
    {
        path: 'group',
        loadChildren: './group/group.module#CjtjGroupModule'
    },
    {
        path: 'group/addGroup',
        loadChildren: './group/group.module#CjtjGroupModule'
    },

    {
        path: 'homeH',
        loadChildren: './homeH/homeH.module#CjtjHomeHModule'
    },
    {
        path: 'location',
        loadChildren: './location/location.module#CjtjLocationModule'
    },
    {
        path: 'creative',
        loadChildren: './creative/creative.module#CjtjCreativeModule'
    }
];

@NgModule({
    imports: [
        RouterModule.forRoot(APP_ROUTES,
            { useHash: true, enableTracing: DEBUG_INFO_ENABLED }
        )
    ],
    exports: [RouterModule]
})
export class CjtjAppRoutingModule {}
