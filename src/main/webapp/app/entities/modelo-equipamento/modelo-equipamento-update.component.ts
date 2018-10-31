import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IModeloEquipamento } from 'app/shared/model/modelo-equipamento.model';
import { ModeloEquipamentoService } from './modelo-equipamento.service';
import { IEquipamento } from 'app/shared/model/equipamento.model';
import { EquipamentoService } from 'app/entities/equipamento';

@Component({
    selector: 'jhi-modelo-equipamento-update',
    templateUrl: './modelo-equipamento-update.component.html'
})
export class ModeloEquipamentoUpdateComponent implements OnInit {
    modeloEquipamento: IModeloEquipamento;
    isSaving: boolean;

    equipamentos: IEquipamento[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private modeloEquipamentoService: ModeloEquipamentoService,
        private equipamentoService: EquipamentoService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ modeloEquipamento }) => {
            this.modeloEquipamento = modeloEquipamento;
        });
        this.equipamentoService.query().subscribe(
            (res: HttpResponse<IEquipamento[]>) => {
                this.equipamentos = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.modeloEquipamento.id !== undefined) {
            this.subscribeToSaveResponse(this.modeloEquipamentoService.update(this.modeloEquipamento));
        } else {
            this.subscribeToSaveResponse(this.modeloEquipamentoService.create(this.modeloEquipamento));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IModeloEquipamento>>) {
        result.subscribe((res: HttpResponse<IModeloEquipamento>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackEquipamentoById(index: number, item: IEquipamento) {
        return item.id;
    }
}
