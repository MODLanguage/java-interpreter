package uk.modl.lenses;

import io.vavr.Tuple;
import io.vavr.Tuple2;

/**
 * Generalisation of the Getter/Setter pattern to support composition and deeply nested immutable objects.
 *
 * @param <S> The Source object type, e.g the type that contains a field of type A
 * @param <A> The field type in S
 * @param <T> The Target object type - often the same as S
 * @param <B> The field type in T - often the same as A
 * @author tonywalmsley
 */
public interface Lens<S, A, T, B> {

    /**
     * Get a field of type A from S
     *
     * @param s the S
     * @return an A
     */
    A getAFromS(final S s);

    /**
     * Transform an A into a B
     *
     * @param a the A
     * @return a B
     */
    B getBFromA(final A a);

    /**
     * Transform an S with a field A into a T with a field B
     *
     * @param s the S
     * @param b the B
     * @return a T
     */
    T getTFromB(final S s, final B b);

    /**
     * Get a field of type B from T
     *
     * @param t the T
     * @return a B
     */
    B getBFromT(final T t);

    /**
     * Transform a B into an A
     *
     * @param b the B
     * @return an A
     */
    A getAFromB(final B b);

    /**
     * Transform a T with a B into an S with an A
     *
     * @param t the T
     * @param a the A
     * @return an S
     */
    S getSFromA(final T t, final A a);

    /**
     * Transform an S into a T
     *
     * @param s the S
     * @return a T
     */
    T getTFromS(final S s);

    /**
     * Transform a T into an S
     *
     * @param t the T
     * @return an S
     */
    S getSFromT(final T t);

    /**
     * Given an S with a field of type A and a new A, transform the new A into a new B and the S into a new T with the new B in place of the old B
     *
     * @param s the S
     * @param a the new A
     * @return a new T with a new B, and the old B
     */
    default Tuple2<T, B> set(final S s, final A a) {
        final B oldB = getBFromA(getAFromS(s));
        final B newB = getBFromA(a);
        final T t = getTFromB(s, newB);
        return Tuple.of(t, oldB);
    }

    /**
     * Create a new Lens that is the composition of this lens and the given lens.
     * <pre>
     *
     * S -> A ... (S) -> AP
     *      |            |
     *      v            v
     * T <- B ... (T) <- BP
     *
     * </pre>
     *
     * @param m    the given Lens - its S and T must be the same type as A and B on this
     * @param <AP> the A for the lens m (AP means A prime)
     * @param <BP> the B for the lens m (BP meand B prime)
     * @return a new Lens such that
     * <pre>
     *
     * S -> AP
     *      |
     *      v
     * T <- BP
     *
     * </pre>
     */
    default <AP, BP> Lens<S, AP, T, BP> then(final Lens<A, AP, B, BP> m) {
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

    /**
     * Support composition as the reverse of Lens.then()
     */
    default <SP, TP> Lens<SP, A, TP, B> compose(final Lens<SP, S, TP, T> m) {
        return m.then(this);
    }

}
