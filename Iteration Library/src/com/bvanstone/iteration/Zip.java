package com.bvanstone.iteration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class Zip<T> implements Iterable<Tuple<T>> {
	List<Iterator<T>> iterators;
	
	@SafeVarargs
	public Zip(Collection<T>... collections) {
		iterators = new ArrayList<>();
		for(Collection<T> c : collections) {
			iterators.add(c.iterator());
		}
	}
	
	@SafeVarargs
	public Zip(Iterator<T>...iterators) {
		this.iterators = Arrays.asList(iterators);
	}
	
	@SafeVarargs
	public Zip(Iterable<T>... iterables) {
		iterators = new ArrayList<>();
		for(Iterable<T> it : iterables) {
			iterators.add(it.iterator());
		}
	}

	@Override
	public Iterator<Tuple<T>> iterator() {
		return new Ziperator();
	}
	
	private class Ziperator implements Iterator<Tuple<T>> {

		@Override
		public boolean hasNext() {
			for(Iterator<T> it : iterators) {
				if(!it.hasNext()) {
					return false;
				}
			}
			return true;
		}

		@Override
		public Tuple<T> next() {
			List<T> temp = new ArrayList<>(iterators.size());
			for(Iterator<T> it : iterators) {
				temp.add(it.next());
			}
			
			return new Tuple<T>(temp);
		}
	}
}
