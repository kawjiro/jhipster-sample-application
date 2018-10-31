import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IManutencao } from 'app/shared/model/manutencao.model';

@Component({
    selector: 'jhi-manutencao-detail',
    templateUrl: './manutencao-detail.component.html'
})
export class ManutencaoDetailComponent implements OnInit {
    manutencao: IManutencao;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ manutencao }) => {
            this.manutencao = manutencao;
        });
    }

    previousState() {
        window.history.back();
    }
}
