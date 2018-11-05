import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Equipamento } from 'app/shared/model/equipamento.model';
import { EquipamentoService } from './equipamento.service';
import { EquipamentoComponent } from './equipamento.component';
import { EquipamentoDetailComponent } from './equipamento-detail.component';
import { EquipamentoUpdateComponent } from './equipamento-update.component';
import { EquipamentoDeletePopupComponent } from './equipamento-delete-dialog.component';
import { IEquipamento } from 'app/shared/model/equipamento.model';

@Injectable({ providedIn: 'root' })
export class EquipamentoResolve implements Resolve<IEquipamento> {
    constructor(private service: EquipamentoService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Equipamento> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Equipamento>) => response.ok),
                map((equipamento: HttpResponse<Equipamento>) => equipamento.body)
            );
        }
        return of(new Equipamento());
    }
}

export const equipamentoRoute: Routes = [
    {
        path: 'equipamento',
        component: EquipamentoComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'primeiraAplicacaoApp.equipamento.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'equipamento/:id/view',
        component: EquipamentoDetailComponent,
        resolve: {
            equipamento: EquipamentoResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'primeiraAplicacaoApp.equipamento.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'equipamento/new',
        component: EquipamentoUpdateComponent,
        resolve: {
            equipamento: EquipamentoResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'primeiraAplicacaoApp.equipamento.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'equipamento/:id/edit',
        component: EquipamentoUpdateComponent,
        resolve: {
            equipamento: EquipamentoResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'primeiraAplicacaoApp.equipamento.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const equipamentoPopupRoute: Routes = [
    {
        path: 'equipamento/:id/delete',
        component: EquipamentoDeletePopupComponent,
        resolve: {
            equipamento: EquipamentoResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'primeiraAplicacaoApp.equipamento.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
