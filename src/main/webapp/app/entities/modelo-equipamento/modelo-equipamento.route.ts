import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { ModeloEquipamento } from 'app/shared/model/modelo-equipamento.model';
import { ModeloEquipamentoService } from './modelo-equipamento.service';
import { ModeloEquipamentoComponent } from './modelo-equipamento.component';
import { ModeloEquipamentoDetailComponent } from './modelo-equipamento-detail.component';
import { ModeloEquipamentoUpdateComponent } from './modelo-equipamento-update.component';
import { ModeloEquipamentoDeletePopupComponent } from './modelo-equipamento-delete-dialog.component';
import { IModeloEquipamento } from 'app/shared/model/modelo-equipamento.model';

@Injectable({ providedIn: 'root' })
export class ModeloEquipamentoResolve implements Resolve<IModeloEquipamento> {
    constructor(private service: ModeloEquipamentoService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((modeloEquipamento: HttpResponse<ModeloEquipamento>) => modeloEquipamento.body));
        }
        return of(new ModeloEquipamento());
    }
}

export const modeloEquipamentoRoute: Routes = [
    {
        path: 'modelo-equipamento',
        component: ModeloEquipamentoComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'primeiraAplicacaoApp.modeloEquipamento.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'modelo-equipamento/:id/view',
        component: ModeloEquipamentoDetailComponent,
        resolve: {
            modeloEquipamento: ModeloEquipamentoResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'primeiraAplicacaoApp.modeloEquipamento.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'modelo-equipamento/new',
        component: ModeloEquipamentoUpdateComponent,
        resolve: {
            modeloEquipamento: ModeloEquipamentoResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'primeiraAplicacaoApp.modeloEquipamento.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'modelo-equipamento/:id/edit',
        component: ModeloEquipamentoUpdateComponent,
        resolve: {
            modeloEquipamento: ModeloEquipamentoResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'primeiraAplicacaoApp.modeloEquipamento.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const modeloEquipamentoPopupRoute: Routes = [
    {
        path: 'modelo-equipamento/:id/delete',
        component: ModeloEquipamentoDeletePopupComponent,
        resolve: {
            modeloEquipamento: ModeloEquipamentoResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'primeiraAplicacaoApp.modeloEquipamento.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
