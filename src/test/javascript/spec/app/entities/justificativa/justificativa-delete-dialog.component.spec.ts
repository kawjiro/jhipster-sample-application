/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { PrimeiraAplicacaoTestModule } from '../../../test.module';
import { JustificativaDeleteDialogComponent } from 'app/entities/justificativa/justificativa-delete-dialog.component';
import { JustificativaService } from 'app/entities/justificativa/justificativa.service';

describe('Component Tests', () => {
    describe('Justificativa Management Delete Component', () => {
        let comp: JustificativaDeleteDialogComponent;
        let fixture: ComponentFixture<JustificativaDeleteDialogComponent>;
        let service: JustificativaService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [PrimeiraAplicacaoTestModule],
                declarations: [JustificativaDeleteDialogComponent]
            })
                .overrideTemplate(JustificativaDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(JustificativaDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(JustificativaService);
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
