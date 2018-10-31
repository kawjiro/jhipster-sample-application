/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { PrimeiraAplicacaoTestModule } from '../../../test.module';
import { ManutencaoDeleteDialogComponent } from 'app/entities/manutencao/manutencao-delete-dialog.component';
import { ManutencaoService } from 'app/entities/manutencao/manutencao.service';

describe('Component Tests', () => {
    describe('Manutencao Management Delete Component', () => {
        let comp: ManutencaoDeleteDialogComponent;
        let fixture: ComponentFixture<ManutencaoDeleteDialogComponent>;
        let service: ManutencaoService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [PrimeiraAplicacaoTestModule],
                declarations: [ManutencaoDeleteDialogComponent]
            })
                .overrideTemplate(ManutencaoDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ManutencaoDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ManutencaoService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
