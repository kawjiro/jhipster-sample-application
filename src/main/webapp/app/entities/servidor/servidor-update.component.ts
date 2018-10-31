import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IServidor } from 'app/shared/model/servidor.model';
import { ServidorService } from './servidor.service';
import { IUso } from 'app/shared/model/uso.model';
import { UsoService } from 'app/entities/uso';
import { IJustificativa } from 'app/shared/model/justificativa.model';
import { JustificativaService } from 'app/entities/justificativa';
import { IOrgaoPublico } from 'app/shared/model/orgao-publico.model';
import { OrgaoPublicoService } from 'app/entities/orgao-publico';

@Component({
    selector: 'jhi-servidor-update',
    templateUrl: './servidor-update.component.html'
})
export class ServidorUpdateComponent implements OnInit {
    servidor: IServidor;
    isSaving: boolean;

    usos: IUso[];

    justificativas: IJustificativa[];

    orgaopublicos: IOrgaoPublico[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private servidorService: ServidorService,
        private usoService: UsoService,
        private justificativaService: JustificativaService,
        private orgaoPublicoService: OrgaoPublicoService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ servidor }) => {
            this.servidor = servidor;
        });
        this.usoService.query({ filter: 'servidor-is-null' }).subscribe(
            (res: HttpResponse<IUso[]>) => {
                if (!this.servidor.uso || !this.servidor.uso.id) {
                    this.usos = res.body;
                } else {
                    this.usoService.find(this.servidor.uso.id).subscribe(
                        (subRes: HttpResponse<IUso>) => {
                            this.usos = [subRes.body].concat(res.body);
                        },
                        (subRes: HttpErrorResponse) => this.onError(subRes.message)
                    );
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.justificativaService.query({ filter: 'servidor-is-null' }).subscribe(
            (res: HttpResponse<IJustificativa[]>) => {
                if (!this.servidor.justificativa || !this.servidor.justificativa.id) {
                    this.justificativas = res.body;
                } else {
                    this.justificativaService.find(this.servidor.justificativa.id).subscribe(
                        (subRes: HttpResponse<IJustificativa>) => {
                            this.justificativas = [subRes.body].concat(res.body);
                        },
                        (subRes: HttpErrorResponse) => this.onError(subRes.message)
                    );
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.orgaoPublicoService.query().subscribe(
            (res: HttpResponse<IOrgaoPublico[]>) => {
                this.orgaopublicos = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.servidor.id !== undefined) {
            this.subscribeToSaveResponse(this.servidorService.update(this.servidor));
        } else {
            this.subscribeToSaveResponse(this.servidorService.create(this.servidor));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IServidor>>) {
        result.subscribe((res: HttpResponse<IServidor>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackUsoById(index: number, item: IUso) {
        return item.id;
    }

    trackJustificativaById(index: number, item: IJustificativa) {
        return item.id;
    }

    trackOrgaoPublicoById(index: number, item: IOrgaoPublico) {
        return item.id;
    }
}
