/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { PrimeiraAplicacaoTestModule } from '../../../test.module';
import { ModeloEquipamentoDeleteDialogComponent } from 'app/entities/modelo-equipamento/modelo-equipamento-delete-dialog.component';
import { ModeloEquipamentoService } from 'app/entities/modelo-equipamento/modelo-equipamento.service';

describe('Component Tests', () => {
    describe('ModeloEquipamento Management Delete Component', () => {
        let comp: ModeloEquipamentoDeleteDialogComponent;
        let fixture: ComponentFixture<ModeloEquipamentoDeleteDialogComponent>;
        let service: ModeloEquipamentoService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [PrimeiraAplicacaoTestModule],
                declarations: [ModeloEquipamentoDeleteDialogComponent]
            })
                .overrideTemplate(ModeloEquipamentoDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ModeloEquipamentoDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ModeloEquipamentoService);
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
