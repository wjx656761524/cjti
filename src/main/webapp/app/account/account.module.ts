import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import {
    SettingsComponent,
    accountState
} from './';
import {CjtjSharedModule} from "app/shared";

@NgModule({
    imports: [
        CjtjSharedModule,
        RouterModule.forChild(accountState)
    ],
    declarations: [
        SettingsComponent
    ],
    providers: [
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CjtjAccountModule {}
