import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IDoenca, Doenca } from 'app/shared/model/doenca.model';
import { DoencaService } from './doenca.service';
import { IPais } from 'app/shared/model/pais.model';
import { PaisService } from 'app/entities/pais/pais.service';

@Component({
  selector: 'jhi-doenca-update',
  templateUrl: './doenca-update.component.html',
})
export class DoencaUpdateComponent implements OnInit {
  isSaving = false;
  pais: IPais[] = [];
  dataPrimeiroCasoDp: any;
  localPrimeiroCasoDp: any;

  editForm = this.fb.group({
    id: [],
    nome: [],
    criado: [],
    dataPrimeiroCaso: [],
    localPrimeiroCaso: [],
    paisPrimeiroCasoId: [],
  });

  constructor(
    protected doencaService: DoencaService,
    protected paisService: PaisService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ doenca }) => {
      if (!doenca.id) {
        const today = moment().startOf('day');
        doenca.criado = today;
      }

      this.updateForm(doenca);

      this.paisService.query().subscribe((res: HttpResponse<IPais[]>) => (this.pais = res.body || []));
    });
  }

  updateForm(doenca: IDoenca): void {
    this.editForm.patchValue({
      id: doenca.id,
      nome: doenca.nome,
      criado: doenca.criado ? doenca.criado.format(DATE_TIME_FORMAT) : null,
      dataPrimeiroCaso: doenca.dataPrimeiroCaso,
      localPrimeiroCaso: doenca.localPrimeiroCaso,
      paisPrimeiroCasoId: doenca.paisPrimeiroCasoId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const doenca = this.createFromForm();
    if (doenca.id !== undefined) {
      this.subscribeToSaveResponse(this.doencaService.update(doenca));
    } else {
      this.subscribeToSaveResponse(this.doencaService.create(doenca));
    }
  }

  private createFromForm(): IDoenca {
    return {
      ...new Doenca(),
      id: this.editForm.get(['id'])!.value,
      nome: this.editForm.get(['nome'])!.value,
      criado: this.editForm.get(['criado'])!.value ? moment(this.editForm.get(['criado'])!.value, DATE_TIME_FORMAT) : undefined,
      dataPrimeiroCaso: this.editForm.get(['dataPrimeiroCaso'])!.value,
      localPrimeiroCaso: this.editForm.get(['localPrimeiroCaso'])!.value,
      paisPrimeiroCasoId: this.editForm.get(['paisPrimeiroCasoId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDoenca>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: IPais): any {
    return item.id;
  }
}
