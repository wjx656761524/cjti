import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {SERVER_API_URL} from '../../app.constants';
import {HttpClient, HttpParams} from '@angular/common/http';
import {LocationModel} from "./location.model";
import {DelGroupModel} from "app/shared/adgroup/del-group.model";
import {DelLocationModel} from "app/shared/location/del-location.model";
import {Engine} from "app/location/add/engine";

@Injectable()
export class LocationService {
    private resourceUrl = SERVER_API_URL + 'api';
    constructor(private http: HttpClient) {}

    //查询定向数据
    getLocations(campaignId: number, groupId: number,beginTime:string, endTime:string,syn: boolean): Observable<any> {
        const params = new HttpParams()
            .set('campaignId',campaignId.toString())
            .set('groupId', groupId.toString())
            .set('syn', syn.toString())
            .set('startTime', beginTime)
            .set('endTime', endTime);
        return this.http.get(`${this.resourceUrl}/location/getAll`,  {params});
    }
    //获取报表信息
    getAllReport(campaignId: number, groupId: number,beginTime:string, endTime:string,syn: boolean): Observable<any> {
        const params = new HttpParams()
            .set('campaignId',campaignId.toString())
            .set('groupId', groupId.toString())
            .set('syn', syn.toString())
            .set('startTime', beginTime)
            .set('endTime', endTime);
        return this.http.get(`${this.resourceUrl}/location/getAllReport`,  {params});
    }

    /**
     * 批量修改溢价
     * @param locationModel
     */
    updateLocations(locationModel:Array<LocationModel>): Observable<any> {
        return this.http.post(`${this.resourceUrl}/location/updateLocations`, locationModel);
    }
    /**
     * 删除广告位
     * @param {Array<DelLocationModel>} delLocationList
     * @returns {Observable<any>}
     */
    delLocation(delLocationList: Array<DelLocationModel>): Observable<any> {
        const params = new HttpParams()
        return this.http.post(`${this.resourceUrl}/location/delLocations`, delLocationList, {params});
    }
    //获取指定单元下所有资源位
    getALLLocations(engine:Engine): Observable<any> {

        return this.http.post(`${this.resourceUrl}/location/getAllLocations`, engine);
    }






}
