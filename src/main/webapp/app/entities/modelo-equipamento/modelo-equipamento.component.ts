import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IModeloEquipamento } from 'app/shared/model/modelo-equipamento.model';
import { Principal } from 'app/core';
import { ModeloEquipamentoService } from './modelo-equipamento.service';

@Component({
    selector: 'jhi-modelo-equipamento',
    templateUrl: './modelo-equipamento.component.html'
})
export class ModeloEquipamentoComponent implements OnInit, OnDestroy {
    modeloEquipamentos: IModeloEquipamento[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private modeloEquipamentoService: ModeloEquipamentoService,
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
            this.modeloEquipamentoService
                .search({
                    query: this.currentSearch
                })
                .subscribe(
                    (res: HttpResponse<IModeloEquipamento[]>) => (this.modeloEquipamentos = res.body),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
        }
        this.modeloEquipamentoService.query().subscribe(
            (res: HttpResponse<IModeloEquipamento[]>) => {
                this.modeloEquipamentos = res.body;
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
        this.registerChangeInModeloEquipamentos();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IModeloEquipamento) {
        return item.id;
    }

    registerChangeInModeloEquipamentos() {
        this.eventSubscriber = this.eventManager.subscribe('modeloEquipamentoListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
