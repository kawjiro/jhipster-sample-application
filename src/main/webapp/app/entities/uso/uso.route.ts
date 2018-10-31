import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Uso } from 'app/shared/model/uso.model';
import { UsoService } from './uso.service';
import { UsoComponent } from './uso.component';
import { UsoDetailComponent } from './uso-detail.component';
import { UsoUpdateComponent } from './uso-update.component';
import { UsoDeletePopupComponent } from './uso-delete-dialog.component';
import { IUso } from 'app/shared/model/uso.model';

@Injectable({ providedIn: 'root' })
export class UsoResolve implements Resolve<IUso> {
    constructor(private service: UsoService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((uso: HttpResponse<Uso>) => uso.body));
        }
        return of(new Uso());
    }
}

export const usoRoute: Routes = [
    {
        path: 'uso',
        component: UsoComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'primeiraAplicacaoApp.uso.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'uso/:id/view',
        component: UsoDetailComponent,
        resolve: {
            uso: UsoResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'primeiraAplicacaoApp.uso.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'uso/new',
        component: UsoUpdateComponent,
        resolve: {
            uso: UsoResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'primeiraAplicacaoApp.uso.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'uso/:id/edit',
        component: UsoUpdateComponent,
        resolve: {
            uso: UsoResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'primeiraAplicacaoApp.uso.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const usoPopupRoute: Routes = [
    {
        path: 'uso/:id/delete',
        component: UsoDeletePopupComponent,
        resolve: {
            uso: UsoResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'primeiraAplicacaoApp.uso.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
