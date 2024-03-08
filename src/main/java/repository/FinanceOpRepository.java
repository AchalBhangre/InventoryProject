package repository;

import org.springframework.stereotype.Repository;


import dao.Finance;

import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface FinanceOpRepository extends JpaRepository<Finance, Integer>{

}
