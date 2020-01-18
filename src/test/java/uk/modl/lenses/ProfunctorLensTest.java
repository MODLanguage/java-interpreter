package uk.modl.lenses;

import io.vavr.Tuple2;
import lombok.RequiredArgsConstructor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ProfunctorLensTest {
    public static final String T = "Title";
    private Library library;

    @Before
    public void before() {
        int i = 0;
        library = new Library(new Floor(new Room(
                new Bookcase(new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++)), new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++)), new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++))),
                new Bookcase(new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++)), new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++)), new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++))),
                new Bookcase(new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++)), new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++)), new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++)))
        ), new Room(
                new Bookcase(new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++)), new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++)), new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++))),
                new Bookcase(new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++)), new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++)), new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++))),
                new Bookcase(new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++)), new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++)), new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++)))
        ), new Room(
                new Bookcase(new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++)), new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++)), new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++))),
                new Bookcase(new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++)), new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++)), new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++))),
                new Bookcase(new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++)), new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++)), new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++)))
        )), new Floor(new Room(
                new Bookcase(new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++)), new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++)), new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++))),
                new Bookcase(new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++)), new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++)), new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++))),
                new Bookcase(new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++)), new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++)), new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++)))
        ), new Room(
                new Bookcase(new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++)), new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++)), new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++))),
                new Bookcase(new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++)), new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++)), new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++))),
                new Bookcase(new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++)), new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++)), new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++)))
        ), new Room(
                new Bookcase(new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++)), new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++)), new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++))),
                new Bookcase(new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++)), new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++)), new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++))),
                new Bookcase(new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++)), new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++)), new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++)))
        )), new Floor(new Room(
                new Bookcase(new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++)), new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++)), new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++))),
                new Bookcase(new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++)), new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++)), new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++))),
                new Bookcase(new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++)), new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++)), new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++)))
        ), new Room(
                new Bookcase(new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++)), new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++)), new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++))),
                new Bookcase(new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++)), new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++)), new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++))),
                new Bookcase(new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++)), new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++)), new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++)))
        ), new Room(
                new Bookcase(new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++)), new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++)), new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++))),
                new Bookcase(new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++)), new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++)), new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++))),
                new Bookcase(new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++)), new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++)), new Shelf(new Book(T + i++), new Book(T + i++), new Book(T + i++)))
        )));
    }

    @Test
    public void test_1() {
        final ProfunctorLens<Shelf, Book, Shelf, Book> lens1 = new ShelfBook1();
        final ProfunctorLens<Bookcase, Shelf, Bookcase, Shelf> lens2 = new BookcaseShelf1();
        final ProfunctorLens<Bookcase, Book, Bookcase, Book> lens3 = lens2.andThenLens(lens1);

        final Tuple2<Bookcase, Book> updated = lens3.set(library.floor1.room1.bookcase1, new Book("New Book"));

        Assert.assertNotNull(updated);
        Assert.assertNotNull(updated._1);
        Assert.assertNotNull(updated._2);
    }
}

@RequiredArgsConstructor
class Library {
    public final Floor floor0;
    public final Floor floor1;
    public final Floor floor2;
}

@RequiredArgsConstructor
class Floor {
    public final Room room0;
    public final Room room1;
    public final Room room2;
}

@RequiredArgsConstructor
class Room {
    final Bookcase bookcase0;
    final Bookcase bookcase1;
    final Bookcase bookcase2;
}

@RequiredArgsConstructor
class Bookcase {
    final Shelf shelf0;
    final Shelf shelf1;
    final Shelf shelf2;
}

@RequiredArgsConstructor
class Shelf {
    final Book book0;
    final Book book1;
    final Book book2;
}

@RequiredArgsConstructor
class Book {
    final String title;
}

class ShelfBook1 implements ProfunctorLens<Shelf, Book, Shelf, Book> {
    @Override
    public Book getAFromS(final Shelf shelf) {
        return shelf.book1;
    }

    @Override
    public Book getBFromA(final Book book) {
        return book;
    }

    @Override
    public Shelf getTFromB(final Shelf shelf, final Book book) {
        return new Shelf(shelf.book0, book, shelf.book2);
    }

    @Override
    public Book getBFromT(final Shelf shelf) {
        return shelf.book1;
    }

    @Override
    public Book getAFromB(final Book book) {
        return book;
    }

    @Override
    public Shelf getSFromA(final Shelf shelf, final Book book) {
        return getTFromB(shelf, book);
    }

    @Override
    public Shelf getTFromS(final Shelf shelf) {
        return new Shelf(shelf.book0, shelf.book1, shelf.book2);
    }

    @Override
    public Shelf getSFromT(final Shelf shelf) {
        return new Shelf(shelf.book0, shelf.book1, shelf.book2);
    }
}

class BookcaseShelf1 implements ProfunctorLens<Bookcase, Shelf, Bookcase, Shelf> {
    @Override
    public Shelf getAFromS(final Bookcase bookcase) {
        return bookcase.shelf1;
    }

    @Override
    public Shelf getBFromA(final Shelf shelf) {
        return shelf;
    }

    @Override
    public Bookcase getTFromB(final Bookcase bookcase, final Shelf shelf) {
        return new Bookcase(bookcase.shelf0, shelf, bookcase.shelf2);
    }

    @Override
    public Shelf getBFromT(final Bookcase bookcase) {
        return bookcase.shelf1;
    }

    @Override
    public Shelf getAFromB(final Shelf shelf) {
        return shelf;
    }

    @Override
    public Bookcase getSFromA(final Bookcase bookcase, final Shelf shelf) {
        return getTFromB(bookcase, shelf);
    }

    @Override
    public Bookcase getTFromS(final Bookcase bookcase) {
        return new Bookcase(bookcase.shelf0, bookcase.shelf1, bookcase.shelf2);
    }

    @Override
    public Bookcase getSFromT(final Bookcase bookcase) {
        return new Bookcase(bookcase.shelf0, bookcase.shelf1, bookcase.shelf2);
    }
}