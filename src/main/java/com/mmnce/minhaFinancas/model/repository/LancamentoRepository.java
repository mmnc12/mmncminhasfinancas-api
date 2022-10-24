package com.mmnce.minhaFinancas.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mmnce.minhaFinancas.model.entity.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

}
