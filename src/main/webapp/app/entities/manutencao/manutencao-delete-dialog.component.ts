import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IManutencao } from 'app/shared/model/manutencao.model';
import { ManutencaoService } from './manutencao.service';

@Component({
    selector: 'jhi-manutencao-delete-dialog',
    templateUrl: './manutencao-delete-dialog.component.html'
})
export class ManutencaoDeleteDialogComponent {
    manutencao: IManutencao;

    constructor(private manutencaoService: ManutencaoService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.manutencaoService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'manutencaoListModification',
                content: 'Deleted an manutencao'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-manutencao-delete-popup',
    template: ''
})
export class ManutencaoDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ manutencao }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(ManutencaoDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.manutencao = manutencao;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
