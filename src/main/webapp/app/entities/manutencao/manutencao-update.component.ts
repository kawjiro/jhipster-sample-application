import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IManutencao } from 'app/shared/model/manutencao.model';
import { ManutencaoService } from './manutencao.service';
import { IEquipamento } from 'app/shared/model/equipamento.model';
import { EquipamentoService } from 'app/entities/equipamento';

@Component({
    selector: 'jhi-manutencao-update',
    templateUrl: './manutencao-update.component.html'
})
export class ManutencaoUpdateComponent implements OnInit {
    manutencao: IManutencao;
    isSaving: boolean;

    equipamentos: IEquipamento[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private manutencaoService: ManutencaoService,
        private equipamentoService: EquipamentoService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ manutencao }) => {
            this.manutencao = manutencao;
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
        if (this.manutencao.id !== undefined) {
            this.subscribeToSaveResponse(this.manutencaoService.update(this.manutencao));
        } else {
            this.subscribeToSaveResponse(this.manutencaoService.create(this.manutencao));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IManutencao>>) {
        result.subscribe((res: HttpResponse<IManutencao>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
