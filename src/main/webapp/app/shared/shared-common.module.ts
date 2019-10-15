import { NgModule } from '@angular/core';

import { CjtjSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
    imports: [CjtjSharedLibsModule],
    declarations: [JhiAlertComponent, JhiAlertErrorComponent],
    exports: [CjtjSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class CjtjSharedCommonModule {}
