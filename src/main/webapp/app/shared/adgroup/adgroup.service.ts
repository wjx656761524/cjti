
import {Injectable} from '@angular/core';

import {SERVER_API_URL} from '../../app.constants';
import {HttpClient, HttpParams} from '@angular/common/http';

import {Observable} from "rxjs";
import {DelGroupModel} from "app/shared/adgroup/del-group.model";
import {AdgroupStatus} from "app/shared/adgroup/adgroup.model";
import {HttpService} from "app/crowd/serivce/http.service";
import {UpdateGroupModel} from "app/shared/adgroup/update-group";


@Injectable(
    {providedIn: 'root',}
)
export class AdgroupService {


    private resourceUrl = SERVER_API_URL + 'api';
    constructor(private http: HttpClient,
                private httsrv: HttpService) {

    }


    /**
     * 获取推广单元信息
     * @param {number}campaignId
     * @param {string} startTime
     * @param {string} endTime
     * @param {boolean} syn
     * @returns {Observable<any>}
     */
    getAll(startTime?: string, endTime?: string,syn?: boolean,campaignId?:string): Observable<any> {

        const params = new HttpParams()
            .set('startTime', startTime)
            .set('endTime', endTime)
            .set('syn',syn.toString())
            .set('campaignId',campaignId)
        console.log(params)
        return this.http.get(`${this.resourceUrl}/group/getAll`,{params})
    }
    /**
     * 获取推广单元信息
     * @param {number}campaignId
     * @param {string} startTime
     * @param {string} endTime
     * @param {boolean} syn
     * @returns {Observable<any>}
     */
    getAllReport(startTime?: string, endTime?: string,syn?: boolean,campaignId?:string): Observable<any> {

        const params = new HttpParams()
            .set('startTime', startTime)
            .set('endTime', endTime)
            .set('syn',syn.toString())
            .set('campaignId',campaignId)
        console.log(params)
        return this.http.get(`${this.resourceUrl}/group/getAllReport`,{params})
    }



    /**
     * 根据计划Id获取推广单元信息
     * @param {number}campaignId
     * @param {string} startTime
     * @param {string} endTime
     * @param {boolean} syn
     * @returns {Observable<any>}
     */
    getGroupbyId(startTime?: string, endTime?: string,syn?: boolean,campaignId?:Number): Observable<any> {

        const params = new HttpParams()
            .set('startTime', startTime)
            .set('endTime', endTime)
            .set('syn',syn.toString())
            .set('campaignId',campaignId.toString())

        return this.http.get(`${this.resourceUrl}/group/getGroupbyId`, {params});
    }
    /**
     * 删除推广单元
     * @param {Array<DelGroupModel>} delUnitList
     * @returns {Observable<any>}
     */
    delGroup(delGroupList: Array<DelGroupModel>): Observable<any> {
        const params = new HttpParams()
        return this.http.post(`${this.resourceUrl}/group/delGroups`, delGroupList, {params});
    }

    /**
     * 修改推广单元优化状态
     * @param { updateGroupStatusModel}
     * @returns {Observable<any>}
     */
     updateGroupStatus(updateGroupStatusModelList:Array<UpdateGroupModel>): Observable<any> {
         return this.http.post(`${this.resourceUrl}/group/updateStatus`, updateGroupStatusModelList);
     }

    /**
     * 添加单元
     * @param adgroupStatus
     */
    addGroup(adgroupStatus:Array<AdgroupStatus>): Observable<any> {
        return this.http.post(`${this.resourceUrl}/group/addGroup`, adgroupStatus);
    }

    updateGroupName(updateGroupName: UpdateGroupModel): Observable<any> {
        return this.http.post(`${this.resourceUrl}/group/updateGroupName`, updateGroupName);
    }
}
