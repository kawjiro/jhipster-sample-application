import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IModeloEquipamento } from 'app/shared/model/modelo-equipamento.model';

type EntityResponseType = HttpResponse<IModeloEquipamento>;
type EntityArrayResponseType = HttpResponse<IModeloEquipamento[]>;

@Injectable({ providedIn: 'root' })
export class ModeloEquipamentoService {
    public resourceUrl = SERVER_API_URL + 'api/modelo-equipamentos';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/modelo-equipamentos';

    constructor(private http: HttpClient) {}

    create(modeloEquipamento: IModeloEquipamento): Observable<EntityResponseType> {
        return this.http.post<IModeloEquipamento>(this.resourceUrl, modeloEquipamento, { observe: 'response' });
    }

    update(modeloEquipamento: IModeloEquipamento): Observable<EntityResponseType> {
        return this.http.put<IModeloEquipamento>(this.resourceUrl, modeloEquipamento, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IModeloEquipamento>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IModeloEquipamento[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IModeloEquipamento[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
