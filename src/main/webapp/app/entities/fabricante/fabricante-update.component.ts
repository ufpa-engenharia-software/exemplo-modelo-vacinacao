import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IFabricante, Fabricante } from 'app/shared/model/fabricante.model';
import { FabricanteService } from './fabricante.service';
import { IPais } from 'app/shared/model/pais.model';
import { PaisService } from 'app/entities/pais/pais.service';
import { IVacina } from 'app/shared/model/vacina.model';
import { VacinaService } from 'app/entities/vacina/vacina.service';

type SelectableEntity = IPais | IVacina;

@Component({
  selector: 'jhi-fabricante-update',
  templateUrl: './fabricante-update.component.html',
})
export class FabricanteUpdateComponent implements OnInit {
  isSaving = false;
  pais: IPais[] = [];
  vacinas: IVacina[] = [];

  editForm = this.fb.group({
    id: [],
    nome: [],
    criado: [],
    paisId: [],
    vacinas: [],
  });

  constructor(
    protected fabricanteService: FabricanteService,
    protected paisService: PaisService,
    protected vacinaService: VacinaService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fabricante }) => {
      if (!fabricante.id) {
        const today = moment().startOf('day');
        fabricante.criado = today;
      }

      this.updateForm(fabricante);

      this.paisService.query().subscribe((res: HttpResponse<IPais[]>) => (this.pais = res.body || []));

      this.vacinaService.query().subscribe((res: HttpResponse<IVacina[]>) => (this.vacinas = res.body || []));
    });
  }

  updateForm(fabricante: IFabricante): void {
    this.editForm.patchValue({
      id: fabricante.id,
      nome: fabricante.nome,
      criado: fabricante.criado ? fabricante.criado.format(DATE_TIME_FORMAT) : null,
      paisId: fabricante.paisId,
      vacinas: fabricante.vacinas,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const fabricante = this.createFromForm();
    if (fabricante.id !== undefined) {
      this.subscribeToSaveResponse(this.fabricanteService.update(fabricante));
    } else {
      this.subscribeToSaveResponse(this.fabricanteService.create(fabricante));
    }
  }

  private createFromForm(): IFabricante {
    return {
      ...new Fabricante(),
      id: this.editForm.get(['id'])!.value,
      nome: this.editForm.get(['nome'])!.value,
      criado: this.editForm.get(['criado'])!.value ? moment(this.editForm.get(['criado'])!.value, DATE_TIME_FORMAT) : undefined,
      paisId: this.editForm.get(['paisId'])!.value,
      vacinas: this.editForm.get(['vacinas'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFabricante>>): void {
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

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }

  getSelected(selectedVals: IVacina[], option: IVacina): IVacina {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
