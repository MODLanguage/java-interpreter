package uk.modl.lenses;

import io.vavr.Tuple;
import io.vavr.Tuple2;

public interface Lens<S, A, T, B> {

    A getAFromS(final S s);

    B getBFromA(final A a);

    T getTFromB(final S s, final B b);

    B getBFromT(final T t);

    A getAFromB(final B b);

    S getSFromA(final T t, final A a);

    T getTFromS(final S s);

    S getSFromT(final T t);

    default Tuple2<T, B> set(final S s, final A a) {
        final B oldB = getBFromA(getAFromS(s));
        final B newB = getBFromA(a);
        final T t = getTFromB(s, newB);
        return Tuple.of(t, oldB);
    }

    default <AP, BP> Lens<S, AP, T, BP> andThenLens(final Lens<A, AP, B, BP> m) {
        return new Lens<S, AP, T, BP>() {

            @Override
            public AP getAFromS(final S s) {
                return m.getAFromS(Lens.this.getAFromS(s));
            }

            @Override
            public BP getBFromA(final AP ap) {
                return m.getBFromA(ap);
            }

            @Override
            public T getTFromB(final S s, final BP bp) {
                return Lens.this.getTFromB(s, m.getTFromB(Lens.this.getAFromS(s), bp));
            }

            @Override
            public BP getBFromT(final T t) {
                return m.getBFromT(Lens.this.getBFromT(t));
            }

            @Override
            public AP getAFromB(final BP bp) {
                return m.getAFromB(bp);
            }

            @Override
            public S getSFromA(final T t, final AP ap) {
                return Lens.this.getSFromA(t, m.getSFromA(Lens.this.getBFromT(t), ap));
            }

            @Override
            public T getTFromS(final S s) {
                final A a = Lens.this.getAFromS(s);
                return Lens.this.getTFromB(s, m.getTFromB(a, m.getBFromA(m.getAFromS(a))));
            }

            @Override
            public S getSFromT(final T t) {
                final B b = Lens.this.getBFromT(t);
                return Lens.this.getSFromA(t, m.getSFromA(b, m.getAFromB(m.getBFromT(b))));
            }

            @Override
            public Tuple2<T, BP> set(final S s, final AP ap) {
                final A sp = Lens.this.getAFromS(s);
                final Tuple2<B, BP> bbp = m.set(sp, ap);
                final Tuple2<T, B> tb = Lens.this.set(s, Lens.this.getAFromB(bbp._1));
                return Tuple.of(tb._1, m.getBFromA(m.getAFromS(sp)));
            }
        };
    }
}
