package com.example.lab3.library.repository;

import com.example.lab3.library.model.Book;
import com.example.lab3.library.model.BookType;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

// Repository in memorie pentru carti
@Repository
public class BookRepository {

    private final Map<Long, Book> books = new ConcurrentHashMap<>();

    public BookRepository() {
        add(new Book(1L, "Ion", "Liviu Rebreanu"));
        add(new Book(2L, "Enigma Otiliei", "George Calinescu"));
        add(new Book(3L, "Morometii", "Marin Preda"));
        add(new Book(4L, "Baltagul", "Mihail Sadoveanu", BookType.REFERENCE));
        add(new Book(5L, "Maitreyi", "Mircea Eliade"));
        add(new Book(6L, "Moara cu noroc", "Ioan Slavici"));
        add(new Book(7L, "Padurea spanzuratilor", "Liviu Rebreanu", BookType.RARE));
        add(new Book(8L, "Ultima noapte de dragoste", "Camil Petrescu", BookType.NEW_RELEASE));
    }

    private void add(Book book) {
        books.put(book.getId(), book);
    }

    public Optional<Book> findById(Long id) {
        return Optional.ofNullable(books.get(id));
    }

    public List<Book> findAll() {
        return books.values().stream().sorted(Comparator.comparing(Book::getId)).toList();
    }

    // Cate carti are imprumutate acum un membru
    public int countBorrowedBy(Long memberId) {
        return (int) books.values().stream().filter(b -> memberId.equals(b.getBorrowedBy())).count();
    }
}
