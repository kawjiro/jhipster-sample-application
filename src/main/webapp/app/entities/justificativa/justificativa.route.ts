import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Justificativa } from 'app/shared/model/justificativa.model';
import { JustificativaService } from './justificativa.service';
import { JustificativaComponent } from './justificativa.component';
import { JustificativaDetailComponent } from './justificativa-detail.component';
import { JustificativaUpdateComponent } from './justificativa-update.component';
import { JustificativaDeletePopupComponent } from './justificativa-delete-dialog.component';
import { IJustificativa } from 'app/shared/model/justificativa.model';

@Injectable({ providedIn: 'root' })
export class JustificativaResolve implements Resolve<IJustificativa> {
    constructor(private service: JustificativaService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Justificativa> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Justificativa>) => response.ok),
                map((justificativa: HttpResponse<Justificativa>) => justificativa.body)
            );
        }
        return of(new Justificativa());
    }
}

export const justificativaRoute: Routes = [
    {
        path: 'justificativa',
        component: JustificativaComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'primeiraAplicacaoApp.justificativa.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'justificativa/:id/view',
        component: JustificativaDetailComponent,
        resolve: {
            justificativa: JustificativaResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'primeiraAplicacaoApp.justificativa.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'justificativa/new',
        component: JustificativaUpdateComponent,
        resolve: {
            justificativa: JustificativaResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'primeiraAplicacaoApp.justificativa.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'justificativa/:id/edit',
        component: JustificativaUpdateComponent,
        resolve: {
            justificativa: JustificativaResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'primeiraAplicacaoApp.justificativa.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const justificativaPopupRoute: Routes = [
    {
        path: 'justificativa/:id/delete',
        component: JustificativaDeletePopupComponent,
        resolve: {
            justificativa: JustificativaResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'primeiraAplicacaoApp.justificativa.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
