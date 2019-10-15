import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import {CjtjSharedModule} from "app/shared";
import {LocationComponent} from "app/location/location.component";
import {locationState} from "app/location/location.route";
import {AddLocationComponent} from "app/location/add/add-location.component";
import {Engine} from "app/location/add/engine";
import {AddMainComponent} from "app/location/add/add-main.component";
import {AddSecondLocationComnpnent} from "app/location/add/addSecond-location.comnpnent";
import {EngineGroup} from "app/group/add/engineGroup";
import {HttpClientModule} from "@angular/common/http";
import {AddThreeCreativeComponent} from "app/group/add/addThree/addThree-creative.component";

@NgModule({
    imports: [
        RouterModule.forChild(locationState),
        CjtjSharedModule,

    ],
    exports: [
        RouterModule
    ],
    declarations: [
        LocationComponent,
        AddLocationComponent,
        AddMainComponent,
        AddSecondLocationComnpnent,

    ],
    providers: [Engine,EngineGroup],
    entryComponents: [AddLocationComponent,AddMainComponent,AddSecondLocationComnpnent,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CjtjLocationModule {}
