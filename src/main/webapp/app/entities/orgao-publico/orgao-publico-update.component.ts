import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IOrgaoPublico } from 'app/shared/model/orgao-publico.model';
import { OrgaoPublicoService } from './orgao-publico.service';

@Component({
    selector: 'jhi-orgao-publico-update',
    templateUrl: './orgao-publico-update.component.html'
})
export class OrgaoPublicoUpdateComponent implements OnInit {
    orgaoPublico: IOrgaoPublico;
    isSaving: boolean;

    constructor(private orgaoPublicoService: OrgaoPublicoService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ orgaoPublico }) => {
            this.orgaoPublico = orgaoPublico;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.orgaoPublico.id !== undefined) {
            this.subscribeToSaveResponse(this.orgaoPublicoService.update(this.orgaoPublico));
        } else {
            this.subscribeToSaveResponse(this.orgaoPublicoService.create(this.orgaoPublico));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IOrgaoPublico>>) {
        result.subscribe((res: HttpResponse<IOrgaoPublico>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
}
