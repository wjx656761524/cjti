import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import {
    campaignState,
    CampaignComponent,
} from './';
import {CjtjSharedModule} from "app/shared";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";

@NgModule({
    imports: [
        RouterModule.forChild(campaignState),
        CjtjSharedModule,
        FormsModule,         // <-- add this
        ReactiveFormsModule,  // <-- and this
    ],
    exports: [
        RouterModule
    ],
    declarations: [
        CampaignComponent
    ],
    providers: [],
    entryComponents: [
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CjtjCampaignModule {}
