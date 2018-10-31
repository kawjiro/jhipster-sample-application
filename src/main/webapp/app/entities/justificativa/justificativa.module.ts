import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PrimeiraAplicacaoSharedModule } from 'app/shared';
import {
    JustificativaComponent,
    JustificativaDetailComponent,
    JustificativaUpdateComponent,
    JustificativaDeletePopupComponent,
    JustificativaDeleteDialogComponent,
    justificativaRoute,
    justificativaPopupRoute
} from './';

const ENTITY_STATES = [...justificativaRoute, ...justificativaPopupRoute];

@NgModule({
    imports: [PrimeiraAplicacaoSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        JustificativaComponent,
        JustificativaDetailComponent,
        JustificativaUpdateComponent,
        JustificativaDeleteDialogComponent,
        JustificativaDeletePopupComponent
    ],
    entryComponents: [
        JustificativaComponent,
        JustificativaUpdateComponent,
        JustificativaDeleteDialogComponent,
        JustificativaDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PrimeiraAplicacaoJustificativaModule {}
