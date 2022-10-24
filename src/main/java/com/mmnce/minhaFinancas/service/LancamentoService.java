package com.mmnce.minhaFinancas.service;

import java.util.List;

import com.mmnce.minhaFinancas.model.entity.Lancamento;
import com.mmnce.minhaFinancas.model.enums.StatusLancamento;

public interface LancamentoService {

	Lancamento salvar( Lancamento lancamento );
	
	Lancamento Atualizar( Lancamento lancamento );
	
	void deletar( Lancamento lancamento );
	
	List<Lancamento> buscar ( Lancamento filtroLancmento );
	
	void atualizarStatus ( Lancamento lancamento, StatusLancamento status );
	
	void validar(Lancamento lancamento);
}