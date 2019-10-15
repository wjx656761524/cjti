import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import {AuthServerProvider} from "app/shared/auth/auth-session.service";
import {StateStorageService} from "app/shared/auth/state-storage.service";

@Injectable()
export class AuthExpiredInterceptor implements HttpInterceptor {
    constructor(
        private authServerProvider: AuthServerProvider,
        private stateStorageService: StateStorageService
    ) {}

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        return next.handle(request).pipe(
            tap(
                (event: HttpEvent<any>) => {},
                (err: any) => {
                    if (err instanceof HttpErrorResponse) {
                        if (err.status === 401 && err.url && !err.url.includes('/api/account')) {
                            const destination = this.stateStorageService.getDestinationState();
                            if (destination !== null) {
                                const to = destination.destination;
                                const toParams = destination.params;
                                if (to.name === 'accessdenied') {
                                    this.stateStorageService.storePreviousState(to.name, toParams);
                                }
                            } else {
                                this.stateStorageService.storeUrl('/');
                            }

                            this.authServerProvider.logout();
                        }
                    }
                }
            )
        );
    }
}
