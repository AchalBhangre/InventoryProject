package repository;

import org.springframework.stereotype.Repository;

import controller.Book;

import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface FinanceOpRepository extends JpaRepository<Book, Integer>{

}
