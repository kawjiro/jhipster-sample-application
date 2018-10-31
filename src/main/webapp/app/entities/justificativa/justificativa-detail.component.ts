import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IJustificativa } from 'app/shared/model/justificativa.model';

@Component({
    selector: 'jhi-justificativa-detail',
    templateUrl: './justificativa-detail.component.html'
})
export class JustificativaDetailComponent implements OnInit {
    justificativa: IJustificativa;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ justificativa }) => {
            this.justificativa = justificativa;
        });
    }

    previousState() {
        window.history.back();
    }
}
