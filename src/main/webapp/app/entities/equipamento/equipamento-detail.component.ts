import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEquipamento } from 'app/shared/model/equipamento.model';

@Component({
    selector: 'jhi-equipamento-detail',
    templateUrl: './equipamento-detail.component.html'
})
export class EquipamentoDetailComponent implements OnInit {
    equipamento: IEquipamento;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ equipamento }) => {
            this.equipamento = equipamento;
        });
    }

    previousState() {
        window.history.back();
    }
}
