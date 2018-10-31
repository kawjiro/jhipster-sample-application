import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IManutencao } from 'app/shared/model/manutencao.model';
import { Principal } from 'app/core';
import { ManutencaoService } from './manutencao.service';

@Component({
    selector: 'jhi-manutencao',
    templateUrl: './manutencao.component.html'
})
export class ManutencaoComponent implements OnInit, OnDestroy {
    manutencaos: IManutencao[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private manutencaoService: ManutencaoService,
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
            this.manutencaoService
                .search({
                    query: this.currentSearch
                })
                .subscribe(
                    (res: HttpResponse<IManutencao[]>) => (this.manutencaos = res.body),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
        }
        this.manutencaoService.query().subscribe(
            (res: HttpResponse<IManutencao[]>) => {
                this.manutencaos = res.body;
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
        this.registerChangeInManutencaos();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IManutencao) {
        return item.id;
    }

    registerChangeInManutencaos() {
        this.eventSubscriber = this.eventManager.subscribe('manutencaoListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
