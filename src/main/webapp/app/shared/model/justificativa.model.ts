import { IServidor } from 'app/shared/model//servidor.model';

export interface IJustificativa {
    id?: number;
    descricaoAtividade?: string;
    servidor?: IServidor;
}

export class Justificativa implements IJustificativa {
    constructor(public id?: number, public descricaoAtividade?: string, public servidor?: IServidor) {}
}
