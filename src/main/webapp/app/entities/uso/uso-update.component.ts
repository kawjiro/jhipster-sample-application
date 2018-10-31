import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { JhiAlertService } from 'ng-jhipster';

import { IUso } from 'app/shared/model/uso.model';
import { UsoService } from './uso.service';
import { IServidor } from 'app/shared/model/servidor.model';
import { ServidorService } from 'app/entities/servidor';

@Component({
    selector: 'jhi-uso-update',
    templateUrl: './uso-update.component.html'
})
export class UsoUpdateComponent implements OnInit {
    uso: IUso;
    isSaving: boolean;

    servidors: IServidor[];
    dataInicioDp: any;
    dataFimDp: any;

    constructor(
        private jhiAlertService: JhiAlertService,
        private usoService: UsoService,
        private servidorService: ServidorService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ uso }) => {
            this.uso = uso;
        });
        this.servidorService.query().subscribe(
            (res: HttpResponse<IServidor[]>) => {
                this.servidors = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.uso.id !== undefined) {
            this.subscribeToSaveResponse(this.usoService.update(this.uso));
        } else {
            this.subscribeToSaveResponse(this.usoService.create(this.uso));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IUso>>) {
        result.subscribe((res: HttpResponse<IUso>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackServidorById(index: number, item: IServidor) {
        return item.id;
    }
}
