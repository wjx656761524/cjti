import {Injectable} from '@angular/core';

import {SERVER_API_URL} from '../../app.constants';
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {UpdateCampaignStatusModel} from "app/shared/campaign/update-campaign-statues";
import {AddCampaignModel} from "app/shared/campaign/add-campaign.model";

@Injectable()
export class CampaignService {
    private resourceUrl = SERVER_API_URL + 'api/campaign';

    constructor(private http: HttpClient) {
    }


    getAll(startTime?: string, endTime?: string, syn?: boolean): Observable<any> {
        let params = new HttpParams()
            .set("startTime", startTime)
            .set("endTime", endTime)
            .set("syn", syn.toString());
        return this.http.get(`${this.resourceUrl}/getAll`, {params});
    }

    /**
     * 根据计划ID获取计划信息
     * @param campaignId
     */
    getOneCampaign(campaignId: number): Observable<any> {
        let params = new HttpParams()
            .set("campaignId", campaignId.toString());
        return this.http.get(`${this.resourceUrl}/getOneCampaign`, {params});
    }

    /**
     * 获取所有计划列表
     */
    getCampaignStatus(): Observable<any> {
        return this.http.get(`${this.resourceUrl}/getCampaignStatus`);
    }


    /**
     * 获取所有
     */
    getCampaign(Params: UpdateCampaignStatusModel): Observable<any> {
        return this.http.get(`${this.resourceUrl}/getAll`);
    }

    /**
     * 添加计划
     */
    addCampaign(addCampaignModel:AddCampaignModel): Observable<any>{
        console.log(addCampaignModel)

        return this.http.post(`${this.resourceUrl}/addCampaign`, addCampaignModel)

    }


    /**
     *修改推广计划状态
     * @param updateCampaignStatusModel
     */
    updateCampaignStatus(updateCampaignStatusModel:UpdateCampaignStatusModel): Observable<any>{
        console.log(updateCampaignStatusModel.campaignId);
        return this.http.post(`${this.resourceUrl}/status`, updateCampaignStatusModel)
    }

    /**
     * 修改出价
     * @param qParams
     */
    updatePrice (qParams: UpdateCampaignStatusModel): Observable<any> {
        return this.http.post(`${this.resourceUrl}/updateModify`,{campaignId:qParams.campaignId,dayBudget:qParams.dayBudget});
    }


    /**
     * 修改计划信息
     * @param qParams
     */
    updateName (qParams: UpdateCampaignStatusModel): Observable<any> {
        return this.http.post(`${this.resourceUrl}/updateModifybind`,{campaignId:qParams.campaignId,campaignName:qParams.campaignName});
    }


    /**
     * 批量修改日限额
     * @param qParams
     */
    updateBatchPrice(qParams: UpdateCampaignStatusModel): Observable<any> {
        return this.http.post(`${this.resourceUrl}/updatePai`,qParams);
    }

    /**
     * 批量修改状态
     */
    updateBatchStatus(qParams: UpdateCampaignStatusModel): Observable<any>{
        return this.http.post(`${this.resourceUrl}/updateCam`, qParams);
    }


}
