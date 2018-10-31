import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IJustificativa } from 'app/shared/model/justificativa.model';
import { JustificativaService } from './justificativa.service';
import { IServidor } from 'app/shared/model/servidor.model';
import { ServidorService } from 'app/entities/servidor';

@Component({
    selector: 'jhi-justificativa-update',
    templateUrl: './justificativa-update.component.html'
})
export class JustificativaUpdateComponent implements OnInit {
    justificativa: IJustificativa;
    isSaving: boolean;

    servidors: IServidor[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private justificativaService: JustificativaService,
        private servidorService: ServidorService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ justificativa }) => {
            this.justificativa = justificativa;
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
        if (this.justificativa.id !== undefined) {
            this.subscribeToSaveResponse(this.justificativaService.update(this.justificativa));
        } else {
            this.subscribeToSaveResponse(this.justificativaService.create(this.justificativa));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IJustificativa>>) {
        result.subscribe((res: HttpResponse<IJustificativa>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
