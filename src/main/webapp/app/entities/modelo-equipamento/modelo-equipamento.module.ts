import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PrimeiraAplicacaoSharedModule } from 'app/shared';
import {
    ModeloEquipamentoComponent,
    ModeloEquipamentoDetailComponent,
    ModeloEquipamentoUpdateComponent,
    ModeloEquipamentoDeletePopupComponent,
    ModeloEquipamentoDeleteDialogComponent,
    modeloEquipamentoRoute,
    modeloEquipamentoPopupRoute
} from './';

const ENTITY_STATES = [...modeloEquipamentoRoute, ...modeloEquipamentoPopupRoute];

@NgModule({
    imports: [PrimeiraAplicacaoSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ModeloEquipamentoComponent,
        ModeloEquipamentoDetailComponent,
        ModeloEquipamentoUpdateComponent,
        ModeloEquipamentoDeleteDialogComponent,
        ModeloEquipamentoDeletePopupComponent
    ],
    entryComponents: [
        ModeloEquipamentoComponent,
        ModeloEquipamentoUpdateComponent,
        ModeloEquipamentoDeleteDialogComponent,
        ModeloEquipamentoDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PrimeiraAplicacaoModeloEquipamentoModule {}
