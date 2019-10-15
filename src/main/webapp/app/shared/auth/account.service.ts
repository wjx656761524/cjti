import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { SERVER_API_URL } from '../../app.constants';
import {Observable} from "rxjs";

@Injectable()
export class AccountService  {
    constructor(private http: HttpClient) { }

    get(): Observable<any> {
        return this.http.get(SERVER_API_URL + 'api/account', {observe: 'response'});
    }

    save(account: any): Observable<Object> {
        return this.http.post(SERVER_API_URL + 'api/account', account, {observe: 'response'});
    }

    getAll(): Observable<any> {
        return this.http.get(SERVER_API_URL + 'api/account');
    }

    getAccountBlance(): Observable<any> {
        return this.http.get(SERVER_API_URL + 'api/getAccountBlance');
    }
}
