import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse, HttpHeaders } from '@angular/common/http';
import { SERVER_API_URL } from '../../app.constants';
import {Observable} from "rxjs";


@Injectable()
export class AuthServerProvider {

    constructor(
        private http: HttpClient
    ) {}

    login(credentials): Observable<any> {
        const data = 'j_username=' + encodeURIComponent(credentials.username) +
            '&j_password=' + encodeURIComponent(credentials.password) +
            '&loginType=' + encodeURIComponent(credentials.loginType) +
            '&remember-me=' + credentials.rememberMe + '&submit=Login';
        const headers = new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded');

        return this.http.post(SERVER_API_URL + 'api/authentication', data, { headers });
    }

    logout(): Observable<any> {
        // logout from the server
        return this.http.post(SERVER_API_URL + 'api/logout', {}, { observe: 'response' });
    }
}
