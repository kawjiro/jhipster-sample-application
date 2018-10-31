import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { PrimeiraAplicacaoOrgaoPublicoModule } from './orgao-publico/orgao-publico.module';
import { PrimeiraAplicacaoServidorModule } from './servidor/servidor.module';
import { PrimeiraAplicacaoUsoModule } from './uso/uso.module';
import { PrimeiraAplicacaoEquipamentoModule } from './equipamento/equipamento.module';
import { PrimeiraAplicacaoJustificativaModule } from './justificativa/justificativa.module';
import { PrimeiraAplicacaoReservaModule } from './reserva/reserva.module';
import { PrimeiraAplicacaoModeloEquipamentoModule } from './modelo-equipamento/modelo-equipamento.module';
import { PrimeiraAplicacaoManutencaoModule } from './manutencao/manutencao.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        PrimeiraAplicacaoOrgaoPublicoModule,
        PrimeiraAplicacaoServidorModule,
        PrimeiraAplicacaoUsoModule,
        PrimeiraAplicacaoEquipamentoModule,
        PrimeiraAplicacaoJustificativaModule,
        PrimeiraAplicacaoReservaModule,
        PrimeiraAplicacaoModeloEquipamentoModule,
        PrimeiraAplicacaoManutencaoModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PrimeiraAplicacaoEntityModule {}
