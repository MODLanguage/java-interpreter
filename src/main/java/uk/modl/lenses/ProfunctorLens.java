package uk.modl.lenses;

import io.vavr.Tuple;
import io.vavr.Tuple2;

public interface ProfunctorLens<S, A, T, B> {

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

    default <AP, BP> ProfunctorLens<S, AP, T, BP> andThenLens(final ProfunctorLens<A, AP, B, BP> m) {
        return new ProfunctorLens<S, AP, T, BP>() {

            @Override
            public AP getAFromS(final S s) {
                return m.getAFromS(ProfunctorLens.this.getAFromS(s));
            }

            @Override
            public BP getBFromA(final AP ap) {
                return m.getBFromA(ap);
            }

            @Override
            public T getTFromB(final S s, final BP bp) {
                return ProfunctorLens.this.getTFromB(s, m.getTFromB(ProfunctorLens.this.getAFromS(s), bp));
            }

            @Override
            public BP getBFromT(final T t) {
                return m.getBFromT(ProfunctorLens.this.getBFromT(t));
            }

            @Override
            public AP getAFromB(final BP bp) {
                return m.getAFromB(bp);
            }

            @Override
            public S getSFromA(final T t, final AP ap) {
                return ProfunctorLens.this.getSFromA(t, m.getSFromA(ProfunctorLens.this.getBFromT(t), ap));
            }

            @Override
            public T getTFromS(final S s) {
                final A a = ProfunctorLens.this.getAFromS(s);
                return ProfunctorLens.this.getTFromB(s, m.getTFromB(a, m.getBFromA(m.getAFromS(a))));
            }

            @Override
            public S getSFromT(final T t) {
                final B b = ProfunctorLens.this.getBFromT(t);
                return ProfunctorLens.this.getSFromA(t, m.getSFromA(b, m.getAFromB(m.getBFromT(b))));
            }

            @Override
            public Tuple2<T, BP> set(final S s, final AP ap) {
                final A sp = ProfunctorLens.this.getAFromS(s);
                final Tuple2<B, BP> bbp = m.set(sp, ap);
                final Tuple2<T, B> tb = ProfunctorLens.this.set(s, ProfunctorLens.this.getAFromB(bbp._1));
                return Tuple.of(tb._1, m.getBFromA(m.getAFromS(sp)));
            }
        };
    }
}
