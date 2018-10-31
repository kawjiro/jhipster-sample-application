import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IUso } from 'app/shared/model/uso.model';
import { Principal } from 'app/core';
import { UsoService } from './uso.service';

@Component({
    selector: 'jhi-uso',
    templateUrl: './uso.component.html'
})
export class UsoComponent implements OnInit, OnDestroy {
    usos: IUso[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private usoService: UsoService,
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
            this.usoService
                .search({
                    query: this.currentSearch
                })
                .subscribe((res: HttpResponse<IUso[]>) => (this.usos = res.body), (res: HttpErrorResponse) => this.onError(res.message));
            return;
        }
        this.usoService.query().subscribe(
            (res: HttpResponse<IUso[]>) => {
                this.usos = res.body;
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
        this.registerChangeInUsos();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IUso) {
        return item.id;
    }

    registerChangeInUsos() {
        this.eventSubscriber = this.eventManager.subscribe('usoListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
