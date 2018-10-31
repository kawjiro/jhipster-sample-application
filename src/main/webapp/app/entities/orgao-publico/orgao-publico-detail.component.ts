import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOrgaoPublico } from 'app/shared/model/orgao-publico.model';

@Component({
    selector: 'jhi-orgao-publico-detail',
    templateUrl: './orgao-publico-detail.component.html'
})
export class OrgaoPublicoDetailComponent implements OnInit {
    orgaoPublico: IOrgaoPublico;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ orgaoPublico }) => {
            this.orgaoPublico = orgaoPublico;
        });
    }

    previousState() {
        window.history.back();
    }
}
