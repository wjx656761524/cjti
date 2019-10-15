import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import {CjtjSharedModule} from "app/shared";
import {creativeState} from "app/creative/creative.route";
import {CreativeComponent} from "app/creative/creative.component";
import {AddCreativeComponent} from "app/creative/add/add-creative.component";
import {EngineCreative} from "app/creative/add/engineCreative";
import {AddMainCreativeComponent} from "app/creative/add/add-MainCreative.component";
import {AddCreativeSecondComponent} from "app/creative/add/add-creativeSecond.component";


@NgModule({
    imports: [
        RouterModule.forChild(creativeState),
        CjtjSharedModule
    ],
    exports: [
        RouterModule
    ],
    declarations: [
       CreativeComponent,
        AddCreativeComponent,
        AddCreativeSecondComponent ,
        AddMainCreativeComponent
    ],
    providers: [EngineCreative],
    entryComponents: [AddCreativeComponent,AddCreativeSecondComponent ,AddMainCreativeComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CjtjCreativeModule {}
