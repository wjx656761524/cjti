import {Injectable} from "@angular/core";
import {SERVER_API_URL} from "app/app.constants";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {stat} from "fs";


@Injectable()
export class CrowdReportService {
    private resourceUrl = SERVER_API_URL + 'api';
    constructor(private http: HttpClient) {}


    //查询定向数据
    getCrowdReport(campaignId: number, adgroupId: number, beginTime: string, endTime: string): Observable<any> {
        const params = new HttpParams()
            .set('campaignId','')
            .set('adgroupId','')
            .set('startTime', beginTime)
            .set('endTime', endTime);
        return this.http.get(`${this.resourceUrl}/crowd/getCrowdRptdailylist`,  {params});
    }

    //查询合并定向数据
    getCrowdlist(campaignId:number,adgroupId:number,beginTime: string, endTime: string): Observable<any> {
        const params = new HttpParams()
            .set('campaignId','')
            .set('crowdId','')
            .set('startTime',beginTime)
            .set('endTime', endTime);
        return this.http.get(`${this.resourceUrl}/crowd/getCrowdlist`, {params});
    }

}
