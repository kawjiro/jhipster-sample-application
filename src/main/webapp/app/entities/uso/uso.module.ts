import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PrimeiraAplicacaoSharedModule } from 'app/shared';
import {
    UsoComponent,
    UsoDetailComponent,
    UsoUpdateComponent,
    UsoDeletePopupComponent,
    UsoDeleteDialogComponent,
    usoRoute,
    usoPopupRoute
} from './';

const ENTITY_STATES = [...usoRoute, ...usoPopupRoute];

@NgModule({
    imports: [PrimeiraAplicacaoSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [UsoComponent, UsoDetailComponent, UsoUpdateComponent, UsoDeleteDialogComponent, UsoDeletePopupComponent],
    entryComponents: [UsoComponent, UsoUpdateComponent, UsoDeleteDialogComponent, UsoDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PrimeiraAplicacaoUsoModule {}
