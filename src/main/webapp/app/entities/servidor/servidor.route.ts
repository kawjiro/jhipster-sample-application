import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Servidor } from 'app/shared/model/servidor.model';
import { ServidorService } from './servidor.service';
import { ServidorComponent } from './servidor.component';
import { ServidorDetailComponent } from './servidor-detail.component';
import { ServidorUpdateComponent } from './servidor-update.component';
import { ServidorDeletePopupComponent } from './servidor-delete-dialog.component';
import { IServidor } from 'app/shared/model/servidor.model';

@Injectable({ providedIn: 'root' })
export class ServidorResolve implements Resolve<IServidor> {
    constructor(private service: ServidorService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Servidor> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Servidor>) => response.ok),
                map((servidor: HttpResponse<Servidor>) => servidor.body)
            );
        }
        return of(new Servidor());
    }
}

export const servidorRoute: Routes = [
    {
        path: 'servidor',
        component: ServidorComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'primeiraAplicacaoApp.servidor.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'servidor/:id/view',
        component: ServidorDetailComponent,
        resolve: {
            servidor: ServidorResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'primeiraAplicacaoApp.servidor.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'servidor/new',
        component: ServidorUpdateComponent,
        resolve: {
            servidor: ServidorResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'primeiraAplicacaoApp.servidor.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'servidor/:id/edit',
        component: ServidorUpdateComponent,
        resolve: {
            servidor: ServidorResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'primeiraAplicacaoApp.servidor.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const servidorPopupRoute: Routes = [
    {
        path: 'servidor/:id/delete',
        component: ServidorDeletePopupComponent,
        resolve: {
            servidor: ServidorResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'primeiraAplicacaoApp.servidor.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
