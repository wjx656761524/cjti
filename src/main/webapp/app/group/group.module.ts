import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import {
    groupState,
    GroupComponent,
} from './';
import {CjtjSharedModule} from "app/shared";
import {CjtjNotifyComponent} from "app/shared/notify/notify.component";
import {EngineGroup} from "app/group/add/engineGroup";
import {AddSelcampaignComponent} from "app/group/add/add-selcampaign.component";
import {AddMainGroupComponent} from "app/group/add/add-mainGroup.component";
import {AddSecondMainGroupComponent} from "app/group/add/addSecond/addSecond-mainGroup.component";
import {HttpClientModule} from "@angular/common/http";
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {AddSecondItemComponent} from "app/group/add/addSecond/addSecond-Item.component";
import {AddSecondLocationComnpnent} from "app/location/add/addSecond-location.comnpnent";
import {Engine} from "app/location/add/engine";
import {AddSecondDiscountComnpnent} from "app/group/add/addSecond/addSecond-discount.comnpnent";
import {AddThreeCreativeComponent} from "app/group/add/addThree/addThree-creative.component";
import {AddThreeCreativeSecondComponent} from "app/group/add/addThree/addThree-creativeSecond.component";

@NgModule({
    imports: [
        RouterModule.forChild(groupState),
        CjtjSharedModule,
    ],
    exports: [
        RouterModule,
    ],
    declarations: [
        GroupComponent,
        AddSelcampaignComponent,
        AddMainGroupComponent,
        AddSecondMainGroupComponent,
        AddSecondItemComponent,
        AddThreeCreativeComponent,
        AddThreeCreativeSecondComponent,
        AddSecondDiscountComnpnent
    ],
    providers: [EngineGroup,NgbActiveModal,Engine],
    entryComponents: [AddSelcampaignComponent,AddSecondItemComponent,AddThreeCreativeComponent,AddThreeCreativeSecondComponent,AddSecondDiscountComnpnent ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CjtjGroupModule {}
