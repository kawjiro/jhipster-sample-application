import { IServidor } from 'app/shared/model//servidor.model';

export interface IOrgaoPublico {
    id?: number;
    nomeOrgaoPublico?: string;
    servidors?: IServidor[];
}

export class OrgaoPublico implements IOrgaoPublico {
    constructor(public id?: number, public nomeOrgaoPublico?: string, public servidors?: IServidor[]) {}
}
