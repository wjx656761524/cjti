import {Injectable} from "@angular/core";
import {SERVER_API_URL} from "app/app.constants";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable()
export class ReportService {
    private resourceUrl = SERVER_API_URL + 'api';
    constructor(private http: HttpClient) {}


    //查询定向数据
    getReport(beginTime:string, endTime:string,syn: boolean): Observable<any> {
        const params = new HttpParams()
            .set('syn', syn.toString())
            .set('startTime', beginTime)
            .set('endTime', endTime);
        return this.http.get(`${this.resourceUrl}/report/getCampaignReport`,  {params});
    }

}
