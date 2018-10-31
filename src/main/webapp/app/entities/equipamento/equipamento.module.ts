import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PrimeiraAplicacaoSharedModule } from 'app/shared';
import {
    EquipamentoComponent,
    EquipamentoDetailComponent,
    EquipamentoUpdateComponent,
    EquipamentoDeletePopupComponent,
    EquipamentoDeleteDialogComponent,
    equipamentoRoute,
    equipamentoPopupRoute
} from './';

const ENTITY_STATES = [...equipamentoRoute, ...equipamentoPopupRoute];

@NgModule({
    imports: [PrimeiraAplicacaoSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        EquipamentoComponent,
        EquipamentoDetailComponent,
        EquipamentoUpdateComponent,
        EquipamentoDeleteDialogComponent,
        EquipamentoDeletePopupComponent
    ],
    entryComponents: [EquipamentoComponent, EquipamentoUpdateComponent, EquipamentoDeleteDialogComponent, EquipamentoDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PrimeiraAplicacaoEquipamentoModule {}
