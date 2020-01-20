package uk.modl.lenses;

import io.vavr.Tuple2;
import lombok.RequiredArgsConstructor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LensTest {
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
        final Lens<Shelf, Book, Shelf, Book> lens1 = new ShelfBook1();
        final Lens<Bookcase, Shelf, Bookcase, Shelf> lens2 = new BookcaseShelf1();
        final Lens<Room, Bookcase, Room, Bookcase> lens3 = new RoomBookcase1();
        final Lens<Floor, Room, Floor, Room> lens4 = new FloorRoom1();
        final Lens<Library, Floor, Library, Floor> lens5 = new LibraryFloor1();


        final Lens<Library, Book, Library, Book> lens = lens5.then(lens4)
                .then(lens3)
                .then(lens2)
                .then(lens1);
        final Tuple2<Library, Book> updated = lens.set(library, new Book("New Book"));

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

class ShelfBook1 implements Lens<Shelf, Book, Shelf, Book> {
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
        return shelf;
    }

    @Override
    public Shelf getSFromT(final Shelf shelf) {
        return shelf;
    }
}

class BookcaseShelf1 implements Lens<Bookcase, Shelf, Bookcase, Shelf> {
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
        return bookcase;
    }

    @Override
    public Bookcase getSFromT(final Bookcase bookcase) {
        return bookcase;
    }
}


class RoomBookcase1 implements Lens<Room, Bookcase, Room, Bookcase> {
    @Override
    public Bookcase getAFromS(final Room room) {
        return room.bookcase1;
    }

    @Override
    public Bookcase getBFromA(final Bookcase bookcase) {
        return bookcase;
    }

    @Override
    public Room getTFromB(final Room room, final Bookcase bookcase) {
        return new Room(room.bookcase0, bookcase, room.bookcase2);
    }

    @Override
    public Bookcase getBFromT(final Room room) {
        return room.bookcase1;
    }

    @Override
    public Bookcase getAFromB(final Bookcase bookcase) {
        return bookcase;
    }

    @Override
    public Room getSFromA(final Room room, final Bookcase bookcase) {
        return getTFromB(room, bookcase);
    }

    @Override
    public Room getTFromS(final Room room) {
        return room;
    }

    @Override
    public Room getSFromT(final Room room) {
        return room;
    }
}

class FloorRoom1 implements Lens<Floor, Room, Floor, Room> {
    @Override
    public Room getAFromS(final Floor floor) {
        return floor.room1;
    }

    @Override
    public Room getBFromA(final Room room) {
        return room;
    }

    @Override
    public Floor getTFromB(final Floor floor, final Room room) {
        return new Floor(floor.room0, room, floor.room2);
    }

    @Override
    public Room getBFromT(final Floor floor) {
        return floor.room1;
    }

    @Override
    public Room getAFromB(final Room room) {
        return room;
    }

    @Override
    public Floor getSFromA(final Floor floor, final Room room) {
        return getTFromB(floor, room);
    }

    @Override
    public Floor getTFromS(final Floor floor) {
        return floor;
    }

    @Override
    public Floor getSFromT(final Floor floor) {
        return floor;
    }
}

class LibraryFloor1 implements Lens<Library, Floor, Library, Floor> {
    @Override
    public Floor getAFromS(final Library library) {
        return library.floor1;
    }

    @Override
    public Floor getBFromA(final Floor floor) {
        return floor;
    }

    @Override
    public Library getTFromB(final Library library, final Floor floor) {
        return new Library(library.floor0, floor, library.floor2);
    }

    @Override
    public Floor getBFromT(final Library library) {
        return library.floor1;
    }

    @Override
    public Floor getAFromB(final Floor floor) {
        return floor;
    }

    @Override
    public Library getSFromA(final Library library, final Floor floor) {
        return getTFromB(library, floor);
    }

    @Override
    public Library getTFromS(final Library library) {
        return library;
    }

    @Override
    public Library getSFromT(final Library library) {
        return library;
    }
}