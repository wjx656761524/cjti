import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import {HOMEH_ROUTE, HomeHComponent} from './';
import {CjtjSharedModule} from "app/shared";

@NgModule({
    imports: [
        CjtjSharedModule,
        RouterModule.forChild([ HOMEH_ROUTE ])
    ],
    declarations: [
        HomeHComponent
    ],
    entryComponents: [
    ],
    providers: [
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CjtjHomeHModule {}
