import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IOrgaoPublico } from 'app/shared/model/orgao-publico.model';
import { Principal } from 'app/core';
import { OrgaoPublicoService } from './orgao-publico.service';

@Component({
    selector: 'jhi-orgao-publico',
    templateUrl: './orgao-publico.component.html'
})
export class OrgaoPublicoComponent implements OnInit, OnDestroy {
    orgaoPublicos: IOrgaoPublico[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private orgaoPublicoService: OrgaoPublicoService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.currentSearch =
            this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search']
                ? this.activatedRoute.snapshot.params['search']
                : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.orgaoPublicoService
                .search({
                    query: this.currentSearch
                })
                .subscribe(
                    (res: HttpResponse<IOrgaoPublico[]>) => (this.orgaoPublicos = res.body),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
        }
        this.orgaoPublicoService.query().subscribe(
            (res: HttpResponse<IOrgaoPublico[]>) => {
                this.orgaoPublicos = res.body;
                this.currentSearch = '';
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    clear() {
        this.currentSearch = '';
        this.loadAll();
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInOrgaoPublicos();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IOrgaoPublico) {
        return item.id;
    }

    registerChangeInOrgaoPublicos() {
        this.eventSubscriber = this.eventManager.subscribe('orgaoPublicoListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
