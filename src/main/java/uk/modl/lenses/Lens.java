package uk.modl.lenses;

import io.vavr.Tuple;
import io.vavr.Tuple2;

public interface Lens<T, R> {
    R get(final T t);

    Tuple2<T, R> set(final T t, final R r);

    default <R2> Lens<T, R2> andThenLens(final Lens<R, R2> m) {
        return new Lens<T, R2>() {

            @Override
            public R2 get(final T t) {
                return m.get(Lens.this.get(t));
            }

            @Override
            public Tuple2<T, R2> set(final T t, final R2 r2) {

                final R r = Lens.this.get(t);
                final Tuple2<R, R2> set = m.set(r, r2);

                final Tuple2<T, R> x = Lens.this.set(t, set._1);
                final Tuple2<R, R2> y = m.set(x._2, set._2);

                return Tuple.of(x._1, y._2);
            }
        };
    }
}
