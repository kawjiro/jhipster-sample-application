import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IModeloEquipamento } from 'app/shared/model/modelo-equipamento.model';
import { ModeloEquipamentoService } from './modelo-equipamento.service';

@Component({
    selector: 'jhi-modelo-equipamento-delete-dialog',
    templateUrl: './modelo-equipamento-delete-dialog.component.html'
})
export class ModeloEquipamentoDeleteDialogComponent {
    modeloEquipamento: IModeloEquipamento;

    constructor(
        private modeloEquipamentoService: ModeloEquipamentoService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.modeloEquipamentoService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'modeloEquipamentoListModification',
                content: 'Deleted an modeloEquipamento'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-modelo-equipamento-delete-popup',
    template: ''
})
export class ModeloEquipamentoDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ modeloEquipamento }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(ModeloEquipamentoDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.modeloEquipamento = modeloEquipamento;
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
