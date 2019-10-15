import {Component, Inject, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {JhiEventManager} from 'ng-jhipster';
import {Subscription} from "rxjs";
import {LoginService} from "app/shared/login/login.service";
import {DOCUMENT} from "@angular/common";

@Component({
    selector: 'cjtj-accessdenied',
    templateUrl: './accessdenied.component.html',
})
export class AccessdeniedComponent implements OnInit, OnDestroy {

    authenticationError: boolean;
    password: string;
    username: string;
    taobaoAuthUrl: string;
    loginType: any;
    private subscription: Subscription;

    constructor(private loginService: LoginService,
                private router: Router,
                private route: ActivatedRoute,
                private eventManager: JhiEventManager,
                @Inject(DOCUMENT) private document: any) {
    }

    ngOnInit() {
        this.taobaoAuthUrl = 'https://oauth.taobao.com/authorize?response_type=code&client_id=26066051&redirect_uri='
            + this.document.location.origin
            + '/cjtjLogin';
        console.log(this.taobaoAuthUrl);
        this.subscription = this.route.params.subscribe((params) => {
            console.log(params)
            this.loginType = params['loginType'];
        });
    }

    login() {
        this.loginService.login({
            username: this.username,
            password: this.password,
            rememberMe: false,
            loginType: this.loginType || ''
        }).then(() => {
            this.authenticationError = false;
            this.router.navigate(['']);

            this.eventManager.broadcast({
                name: 'authenticationSuccess',
                content: 'Sending Authentication Success'
            });

            this.router.navigate(['']);
        }).catch(() => {
            this.authenticationError = true;
        });
    }

    ngOnDestroy(): void {
        this.subscription.unsubscribe();
    }
}
