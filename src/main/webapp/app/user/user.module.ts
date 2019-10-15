import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import {
    UserComponent, userState,
} from './';
import {CjtjSharedModule} from "app/shared";

@NgModule({
    imports: [
        RouterModule.forChild(userState),
        CjtjSharedModule
    ],
    exports: [
        RouterModule
    ],
    declarations: [
        UserComponent,
    ],
    providers: [],
    entryComponents: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CjtjUserModule {}
