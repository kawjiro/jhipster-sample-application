import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IEquipamento } from 'app/shared/model/equipamento.model';
import { Principal } from 'app/core';
import { EquipamentoService } from './equipamento.service';

@Component({
    selector: 'jhi-equipamento',
    templateUrl: './equipamento.component.html'
})
export class EquipamentoComponent implements OnInit, OnDestroy {
    equipamentos: IEquipamento[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private equipamentoService: EquipamentoService,
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
            this.equipamentoService
                .search({
                    query: this.currentSearch
                })
                .subscribe(
                    (res: HttpResponse<IEquipamento[]>) => (this.equipamentos = res.body),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
        }
        this.equipamentoService.query().subscribe(
            (res: HttpResponse<IEquipamento[]>) => {
                this.equipamentos = res.body;
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
        this.registerChangeInEquipamentos();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IEquipamento) {
        return item.id;
    }

    registerChangeInEquipamentos() {
        this.eventSubscriber = this.eventManager.subscribe('equipamentoListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
