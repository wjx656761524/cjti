import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {SERVER_API_URL} from '../../app.constants';
import {HttpClient, HttpParams} from '@angular/common/http';
import {DelGroupModel} from "app/shared/adgroup/del-group.model";
import {CreativeModel} from "app/shared/creative/creative.model";
import {HttpService} from "app/crowd/serivce/http.service";




@Injectable()
export class CreativeService {
    private resourceUrl = SERVER_API_URL + 'api';
    constructor(private http: HttpClient,private httsrv: HttpService) {}

    //查询定向数据
    getCreativesBygroupId(campaignId: number,groupId: number,beginTime:string, endTime:string,syn: boolean): Observable<any> {
        const params = new HttpParams()
            .set('campaignId', campaignId.toString())
            .set('groupId', groupId.toString())
            .set('syn', syn.toString())
            .set('startTime', beginTime)
            .set('endTime', endTime);
        return this.http.get(`${this.resourceUrl}/creative/getAll`,  {params});
    }
    //查询创意的所有报表
    getCreativesReport(campaignId: number,groupId: number,beginTime:string, endTime:string,syn: boolean): Observable<any> {
        const params = new HttpParams()
            .set('campaignId', campaignId.toString())
            .set('groupId', groupId.toString())
            .set('campaignId',campaignId.toString())
            .set('syn', syn.toString())
            .set('startTime', beginTime)
            .set('endTime', endTime);
        return this.http.get(`${this.resourceUrl}/creative/getCreativesReport`,  {params});
    }


    /**
     * 删除创意
     * @param {Array<CreativeModel>} delCreativeList
     * @returns {Observable<any>}
     */
    delCreative(delCreativeList: Array<CreativeModel>): Observable<any> {
        return this.http.post(`${this.resourceUrl}/creative/delCreative`, delCreativeList);
    }

    /**
     * 绑定创意
     * @param updateCreativeList
     */
    updateCreative(updateCreativeList: Array<CreativeModel>): Observable<any> {
        return this.httsrv.onHttpPost('/api/creative/updateCreative',updateCreativeList);
    }






}
