import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {SERVER_API_URL} from '../../app.constants';
import {HttpClient, HttpParams} from '@angular/common/http';
import {EngineCreative} from "app/creative/add/engineCreative";
import set = Reflect.set;





@Injectable()
export class ItemService {
    private resourceUrl = SERVER_API_URL + 'api';
    constructor(private http: HttpClient) {}

    //查询定向数据
    getItemDetailsByItemId(itemId:number,syn:boolean): Observable<any> {
        const params=new HttpParams()
            .set('itemId',itemId.toString())
            .set('syn',syn.toString())

        return this.http.get(`${this.resourceUrl}/item/getItemDetailsByItemId`,{params});
    }

    /**
     * 获取指定计划下除所有单元绑定宝贝外的所有宝贝
     * @param campaignId
     * @param syn
     * @return
     */
    getItemDetailsByCampaignId(campaignId:number,syn:boolean):Observable<any>{
        const params=new HttpParams()
            .set('campaignId',campaignId.toString())
            .set('syn',syn.toString())
        return this.http.get(`${this.resourceUrl}/item/getItemDetailsByCampaignId`,{params});

    }






}
