import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PrimeiraAplicacaoSharedModule } from 'app/shared';
import {
    ServidorComponent,
    ServidorDetailComponent,
    ServidorUpdateComponent,
    ServidorDeletePopupComponent,
    ServidorDeleteDialogComponent,
    servidorRoute,
    servidorPopupRoute
} from './';

const ENTITY_STATES = [...servidorRoute, ...servidorPopupRoute];

@NgModule({
    imports: [PrimeiraAplicacaoSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ServidorComponent,
        ServidorDetailComponent,
        ServidorUpdateComponent,
        ServidorDeleteDialogComponent,
        ServidorDeletePopupComponent
    ],
    entryComponents: [ServidorComponent, ServidorUpdateComponent, ServidorDeleteDialogComponent, ServidorDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PrimeiraAplicacaoServidorModule {}
