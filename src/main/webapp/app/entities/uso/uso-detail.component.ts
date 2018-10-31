import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IUso } from 'app/shared/model/uso.model';

@Component({
    selector: 'jhi-uso-detail',
    templateUrl: './uso-detail.component.html'
})
export class UsoDetailComponent implements OnInit {
    uso: IUso;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ uso }) => {
            this.uso = uso;
        });
    }

    previousState() {
        window.history.back();
    }
}
