import {Component, OnInit} from "@angular/core";
import {Principal} from "app/shared/auth/principal.service";


@Component({
    selector: 'cjtj-user',
    templateUrl: './user.component.html'
})
export class UserComponent implements OnInit {

    account: any; // 当前账户

    constructor(public principal: Principal) {
    }

    ngOnInit(): void {
        this.principal.identity(false).then((account) => {
            this.account = account;
        });
    }
}
