import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IJustificativa } from 'app/shared/model/justificativa.model';
import { Principal } from 'app/core';
import { JustificativaService } from './justificativa.service';

@Component({
    selector: 'jhi-justificativa',
    templateUrl: './justificativa.component.html'
})
export class JustificativaComponent implements OnInit, OnDestroy {
    justificativas: IJustificativa[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private justificativaService: JustificativaService,
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
            this.justificativaService
                .search({
                    query: this.currentSearch
                })
                .subscribe(
                    (res: HttpResponse<IJustificativa[]>) => (this.justificativas = res.body),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
        }
        this.justificativaService.query().subscribe(
            (res: HttpResponse<IJustificativa[]>) => {
                this.justificativas = res.body;
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
        this.registerChangeInJustificativas();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IJustificativa) {
        return item.id;
    }

    registerChangeInJustificativas() {
        this.eventSubscriber = this.eventManager.subscribe('justificativaListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
