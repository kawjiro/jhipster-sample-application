import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { OrgaoPublico } from 'app/shared/model/orgao-publico.model';
import { OrgaoPublicoService } from './orgao-publico.service';
import { OrgaoPublicoComponent } from './orgao-publico.component';
import { OrgaoPublicoDetailComponent } from './orgao-publico-detail.component';
import { OrgaoPublicoUpdateComponent } from './orgao-publico-update.component';
import { OrgaoPublicoDeletePopupComponent } from './orgao-publico-delete-dialog.component';
import { IOrgaoPublico } from 'app/shared/model/orgao-publico.model';

@Injectable({ providedIn: 'root' })
export class OrgaoPublicoResolve implements Resolve<IOrgaoPublico> {
    constructor(private service: OrgaoPublicoService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((orgaoPublico: HttpResponse<OrgaoPublico>) => orgaoPublico.body));
        }
        return of(new OrgaoPublico());
    }
}

export const orgaoPublicoRoute: Routes = [
    {
        path: 'orgao-publico',
        component: OrgaoPublicoComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'primeiraAplicacaoApp.orgaoPublico.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'orgao-publico/:id/view',
        component: OrgaoPublicoDetailComponent,
        resolve: {
            orgaoPublico: OrgaoPublicoResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'primeiraAplicacaoApp.orgaoPublico.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'orgao-publico/new',
        component: OrgaoPublicoUpdateComponent,
        resolve: {
            orgaoPublico: OrgaoPublicoResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'primeiraAplicacaoApp.orgaoPublico.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'orgao-publico/:id/edit',
        component: OrgaoPublicoUpdateComponent,
        resolve: {
            orgaoPublico: OrgaoPublicoResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'primeiraAplicacaoApp.orgaoPublico.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const orgaoPublicoPopupRoute: Routes = [
    {
        path: 'orgao-publico/:id/delete',
        component: OrgaoPublicoDeletePopupComponent,
        resolve: {
            orgaoPublico: OrgaoPublicoResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'primeiraAplicacaoApp.orgaoPublico.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
