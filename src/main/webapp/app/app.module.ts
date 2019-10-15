import './vendor.ts';

import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import {NgbDatepickerConfig, NgbDatepickerModule} from '@ng-bootstrap/ng-bootstrap';
import { Ng2Webstorage } from 'ngx-webstorage';
import { NgJhipsterModule } from 'ng-jhipster';

import { AuthExpiredInterceptor } from './blocks/interceptor/auth-expired.interceptor';
import { ErrorHandlerInterceptor } from './blocks/interceptor/errorhandler.interceptor';
import { NotificationInterceptor } from './blocks/interceptor/notification.interceptor';
import { CjtjSharedModule } from 'app/shared';
import { CjtjAppRoutingModule } from './app-routing.module';
import { CjtjEntityModule } from './entities/entity.module';
import * as moment from 'moment';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import {NavbarComponent, FooterComponent, PageRibbonComponent, ErrorComponent, CjtjMainComponent} from './layouts';
import {CjtjHomeModule} from "app/home";
import {CjtjAccountModule} from "app/account/account.module";
import {UserRouteAccessService} from "app/shared/auth/user-route-access-service";
import {AccessdeniedComponent} from "app/layouts/error/accessdenied.component";
import {JhiEventManager} from "ng-jhipster";
import {AddMainGroupComponent} from "app/group";

@NgModule({
    imports: [
        BrowserModule,
        Ng2Webstorage.forRoot({ prefix: 'jhi', separator: '-' }),
        NgJhipsterModule.forRoot({
            // set below to true to make alerts look like toast
            alertAsToast: false,
            alertTimeout: 5000
        }),
        CjtjSharedModule.forRoot(),
        CjtjHomeModule,
        CjtjAccountModule,
        // jhipster-needle-angular-add-module JHipster will add new module here
        CjtjEntityModule,
        CjtjAppRoutingModule
    ],
    declarations: [
        CjtjMainComponent,
        NavbarComponent,
        ErrorComponent,
        PageRibbonComponent,
        FooterComponent,
        AccessdeniedComponent,

    ],
    providers: [
        {
            provide: HTTP_INTERCEPTORS,
            useClass: AuthExpiredInterceptor,
            multi: true
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: ErrorHandlerInterceptor,
            multi: true
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: NotificationInterceptor,
            multi: true
        },
        UserRouteAccessService,
        JhiEventManager
    ],
    bootstrap: [CjtjMainComponent]
})
export class CjtjAppModule {
    constructor(private dpConfig: NgbDatepickerConfig) {
        this.dpConfig.minDate = { year: moment().year() - 100, month: 1, day: 1 };
    }
}
