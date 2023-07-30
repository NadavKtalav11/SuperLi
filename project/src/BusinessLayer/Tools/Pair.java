package BusinessLayer.Tools;


    public class Pair <T, E> {

        private T first;
        private E second;

        public Pair(T first, E second) {
            this.first = first;
            this.second = second;
        }

        public T getFirst() {
            return first;
        }

        public E getSecond() {
            return second;
        }

        public void setFirst(T first) {
            this.first = first;
        }

        public void setSecond(E second) {
            this.second = second;
        }

        public boolean equals1(Pair<?, ?> pair2) {
            if (pair2 == null) {
                return false;
            }
            if (this.getFirst() == pair2.getSecond() && this.getSecond() == pair2.getSecond()) {
                return true;
            }
            return false;
        }



        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Pair<?, ?> pair = (Pair<?, ?>) o;
            return (equals1(pair));
        }

        @Override
        public int hashCode() {
            int result = first != null ? first.hashCode() : 0;
            result = 31 * result + (second != null ? second.hashCode() : 0);
            return result;
        }
    }








