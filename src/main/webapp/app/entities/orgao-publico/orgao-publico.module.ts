import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PrimeiraAplicacaoSharedModule } from 'app/shared';
import {
    OrgaoPublicoComponent,
    OrgaoPublicoDetailComponent,
    OrgaoPublicoUpdateComponent,
    OrgaoPublicoDeletePopupComponent,
    OrgaoPublicoDeleteDialogComponent,
    orgaoPublicoRoute,
    orgaoPublicoPopupRoute
} from './';

const ENTITY_STATES = [...orgaoPublicoRoute, ...orgaoPublicoPopupRoute];

@NgModule({
    imports: [PrimeiraAplicacaoSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        OrgaoPublicoComponent,
        OrgaoPublicoDetailComponent,
        OrgaoPublicoUpdateComponent,
        OrgaoPublicoDeleteDialogComponent,
        OrgaoPublicoDeletePopupComponent
    ],
    entryComponents: [
        OrgaoPublicoComponent,
        OrgaoPublicoUpdateComponent,
        OrgaoPublicoDeleteDialogComponent,
        OrgaoPublicoDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PrimeiraAplicacaoOrgaoPublicoModule {}
