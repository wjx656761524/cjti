import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from '@angular/router';
import {Principal} from "app/shared/auth/principal.service";
import {StateStorageService} from "app/shared/auth/state-storage.service";

@Injectable()
export class UserRouteAccessService implements CanActivate {

    constructor(private router: Router,
                private principal: Principal,
                private stateStorageService: StateStorageService) {
    }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean | Promise<boolean> {

        const authorities = route.data['authorities'];
        // We need to call the checkLogin / and so the principal.identity() function, to ensure,
        // that the client has a principal too, if they already logged in by the server.
        // This could happen on a page refresh.
        return this.checkLogin(authorities, state.url);
    }

    checkLogin(authorities: string[], url: string): Promise<boolean> {
        console.log('-----------------------');
        console.log('检查登陆');
        console.log(url);
        console.log(authorities);
        return Promise.resolve(this.principal.identity().then((account) => {

            // console.log("kdjfkd==="+account.authorities)

            if (!authorities || authorities.length === 0) {
                return true;
            }

            if (account) {
                return this.principal.hasAnyAuthority(authorities).then(
                    (response) => {
                        if (response) {
                            return true;
                        }
                        return false;
                    }
                );
            }

            this.stateStorageService.storeUrl(url);
            this.router.navigate(['accessdenied']).then(() => {
                // only show the login dialog, if the user hasn't logged in yet
                // if (!account) {
                //     this.loginModalService.open();
                // }
            });
            return false;
        }));
    }
}
