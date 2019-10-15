import { Routes } from '@angular/router';

import { ErrorComponent } from './error.component';
import {AccessdeniedComponent} from './accessdenied.component';

export const errorRoute: Routes = [
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
    }
];
