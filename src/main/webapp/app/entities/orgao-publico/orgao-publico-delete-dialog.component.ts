import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IOrgaoPublico } from 'app/shared/model/orgao-publico.model';
import { OrgaoPublicoService } from './orgao-publico.service';

@Component({
    selector: 'jhi-orgao-publico-delete-dialog',
    templateUrl: './orgao-publico-delete-dialog.component.html'
})
export class OrgaoPublicoDeleteDialogComponent {
    orgaoPublico: IOrgaoPublico;

    constructor(
        private orgaoPublicoService: OrgaoPublicoService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.orgaoPublicoService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'orgaoPublicoListModification',
                content: 'Deleted an orgaoPublico'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-orgao-publico-delete-popup',
    template: ''
})
export class OrgaoPublicoDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ orgaoPublico }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(OrgaoPublicoDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.orgaoPublico = orgaoPublico;
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
