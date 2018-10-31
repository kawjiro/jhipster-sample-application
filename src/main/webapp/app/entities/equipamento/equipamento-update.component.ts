import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IEquipamento } from 'app/shared/model/equipamento.model';
import { EquipamentoService } from './equipamento.service';
import { IReserva } from 'app/shared/model/reserva.model';
import { ReservaService } from 'app/entities/reserva';

@Component({
    selector: 'jhi-equipamento-update',
    templateUrl: './equipamento-update.component.html'
})
export class EquipamentoUpdateComponent implements OnInit {
    equipamento: IEquipamento;
    isSaving: boolean;

    reservas: IReserva[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private equipamentoService: EquipamentoService,
        private reservaService: ReservaService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ equipamento }) => {
            this.equipamento = equipamento;
        });
        this.reservaService.query().subscribe(
            (res: HttpResponse<IReserva[]>) => {
                this.reservas = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.equipamento.id !== undefined) {
            this.subscribeToSaveResponse(this.equipamentoService.update(this.equipamento));
        } else {
            this.subscribeToSaveResponse(this.equipamentoService.create(this.equipamento));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IEquipamento>>) {
        result.subscribe((res: HttpResponse<IEquipamento>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackReservaById(index: number, item: IReserva) {
        return item.id;
    }
}
