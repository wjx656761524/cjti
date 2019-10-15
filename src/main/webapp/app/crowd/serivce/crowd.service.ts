import { Injectable } from '@angular/core';
import {Observable} from "rxjs";
import {HttpService} from "app/crowd/serivce/http.service";
@Injectable({
  providedIn: 'root'
})
export class CrowdService {
    constructor(private httsrv: HttpService) { }

    // 获取列表内容
    getCrowdRptdailylist = (queryParams: CrowdQuery):Observable<any>  =>{
        return this.httsrv.onHttpGet("/api/crowd/getCrowdRptdailylist",queryParams).pipe(res =>{
            return res;
        });
    }

    // 批量删除
    batchDelete = (crowdId: string,adgroupId: string):Observable<any>  =>{
        return this.httsrv.onHttpGet("/api/crowd/crowdDelete",{crowdId:crowdId,adgroupId:adgroupId}).pipe(res =>{
            return res;
        });
    }


    //修改出价或状态
    updatePrice = (queryParams: ModiFyBind) =>{
        return this.httsrv.onHttpGet("/api/crowd/crowdModifybind",queryParams).pipe(res =>{
            return res;
        });
    }

    getCampaignGroup = () =>{
        return this.httsrv.onHttpGet("/api/crowd/getCampaignGroup",{}).pipe(res =>{
            return res;
        });
    }


    getOptionPage = (targetId: number, targetType: string,itemIds: string) =>{
        return this.httsrv.onHttpGet("/api/crowd/getOptionPage",{targetId: targetId,targetType: targetType ,itemIds: itemIds}).pipe(res =>{
            return res;
        });
    }

    getItemPage = (campaignId: number)=>{
        return this.httsrv.onHttpGet("/api/crowd/getItemPage",{campaignId: campaignId}).pipe(res =>{
            return res;
        });
    }
    saveCrowd = (thaddCrowdParams: addCrowdParem) => {
        if( null == thaddCrowdParams.price){
            alert("请设置出价");
            return;
        }
        if(thaddCrowdParams.checked  == false){
            return;
        }
        return this.httsrv.onHttpPost("/api/crowd/crowdAdd",thaddCrowdParams).pipe(res =>{
            return res;
        });
    }

}

export class ModiFyBind {
    price?:number;
    status?:string;
    crowdId?:number;
    adgroupId?:number;
}

export interface CrowdQuery {
    startTime?:string;
    campaignId?:number;
    adgroupId?:number;
    crowdId?:number;
    endTime?:string;
    itemId?: number
}
export interface addCrowdParem {
    campaignId?:number;
    adgroupId?:number;
    targetId?:number;
    targetType?:string;
    labelId?: number;
    labelValue?: string;
    price?: number;
    optionName?: string;
    checked?:boolean;
    optionValue?: any[];
    crowdName?: string;
    crowdDesc?: string;
    itemId?: number;
}

export interface Price {
    price?: number;
}
export class Offer {
    price?: number;
    optionName?: string;
    checked?:boolean;
    optionValue?: any[];
    crowdName?: string;
    crowdDesc?: string;
}

