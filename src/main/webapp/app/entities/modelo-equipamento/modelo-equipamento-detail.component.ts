import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IModeloEquipamento } from 'app/shared/model/modelo-equipamento.model';

@Component({
    selector: 'jhi-modelo-equipamento-detail',
    templateUrl: './modelo-equipamento-detail.component.html'
})
export class ModeloEquipamentoDetailComponent implements OnInit {
    modeloEquipamento: IModeloEquipamento;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ modeloEquipamento }) => {
            this.modeloEquipamento = modeloEquipamento;
        });
    }

    previousState() {
        window.history.back();
    }
}
