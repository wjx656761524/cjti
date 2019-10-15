import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';

import { VERSION } from 'app/app.constants';
import { ProfileService } from 'app/layouts/profiles/profile.service';
import {Principal} from "app/shared/auth/principal.service";

@Component({
    selector: 'cjtj-navbar',
    templateUrl: './navbar.component.html',
})
export class NavbarComponent implements OnInit {

    constructor(public principal: Principal) {}

    ngOnInit() {
    }

    isAuthenticated() {
        return this.principal.isAuthenticated();
    }
}
