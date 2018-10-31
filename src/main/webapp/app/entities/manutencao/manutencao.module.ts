import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PrimeiraAplicacaoSharedModule } from 'app/shared';
import {
    ManutencaoComponent,
    ManutencaoDetailComponent,
    ManutencaoUpdateComponent,
    ManutencaoDeletePopupComponent,
    ManutencaoDeleteDialogComponent,
    manutencaoRoute,
    manutencaoPopupRoute
} from './';

const ENTITY_STATES = [...manutencaoRoute, ...manutencaoPopupRoute];

@NgModule({
    imports: [PrimeiraAplicacaoSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ManutencaoComponent,
        ManutencaoDetailComponent,
        ManutencaoUpdateComponent,
        ManutencaoDeleteDialogComponent,
        ManutencaoDeletePopupComponent
    ],
    entryComponents: [ManutencaoComponent, ManutencaoUpdateComponent, ManutencaoDeleteDialogComponent, ManutencaoDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PrimeiraAplicacaoManutencaoModule {}
