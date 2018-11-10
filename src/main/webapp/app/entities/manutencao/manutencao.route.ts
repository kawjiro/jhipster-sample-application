import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Manutencao } from 'app/shared/model/manutencao.model';
import { ManutencaoService } from './manutencao.service';
import { ManutencaoComponent } from './manutencao.component';
import { ManutencaoDetailComponent } from './manutencao-detail.component';
import { ManutencaoUpdateComponent } from './manutencao-update.component';
import { ManutencaoDeletePopupComponent } from './manutencao-delete-dialog.component';
import { IManutencao } from 'app/shared/model/manutencao.model';

@Injectable({ providedIn: 'root' })
export class ManutencaoResolve implements Resolve<IManutencao> {
    constructor(private service: ManutencaoService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Manutencao> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Manutencao>) => response.ok),
                map((manutencao: HttpResponse<Manutencao>) => manutencao.body)
            );
        }
        return of(new Manutencao());
    }
}

export const manutencaoRoute: Routes = [
    {
        path: 'manutencao',
        component: ManutencaoComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'primeiraAplicacaoApp.manutencao.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'manutencao/:id/view',
        component: ManutencaoDetailComponent,
        resolve: {
            manutencao: ManutencaoResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'primeiraAplicacaoApp.manutencao.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'manutencao/new',
        component: ManutencaoUpdateComponent,
        resolve: {
            manutencao: ManutencaoResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'primeiraAplicacaoApp.manutencao.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'manutencao/:id/edit',
        component: ManutencaoUpdateComponent,
        resolve: {
            manutencao: ManutencaoResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'primeiraAplicacaoApp.manutencao.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const manutencaoPopupRoute: Routes = [
    {
        path: 'manutencao/:id/delete',
        component: ManutencaoDeletePopupComponent,
        resolve: {
            manutencao: ManutencaoResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'primeiraAplicacaoApp.manutencao.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
