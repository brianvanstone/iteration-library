package com.bvanstone.iteration;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class Tuple<T> implements List<T> {
	private int hashCode = 1;
	private T[] internalData;
	private Map<T, List<Integer>> elements;
	
	private Tuple() {
		elements = new HashMap<>();
	}
	
	@SuppressWarnings("unchecked")
	public Tuple(List<T> list) {
		this();
		internalData = (T[]) list.toArray();
		buildMap();
	}
	
	public Tuple(Collection<T> collection) {
		this();
		internalData = collection.toArray(internalData);
		buildMap();
	}
	
	@SuppressWarnings("unchecked")
	public Tuple(Iterator<T> iterator) {
		this();
		List<T> tempContainer = new LinkedList<>();
		
		while(iterator.hasNext()) {
			tempContainer.add(iterator.next());
		}
		
		internalData = (T[]) tempContainer.toArray();
		buildMap();
	}
	
	public Tuple(Iterable<T> iterable) {
		this(iterable.iterator());
		buildMap();
	}
	
	public Tuple(T[] array) {
		this();
		internalData = Arrays.copyOf(array, array.length);
		buildMap();
	}
	
	private void buildMap() {
		for(int i = 0; i < internalData.length; i++) {
			T item = internalData[i];
			if(!elements.containsKey(item)) {
				elements.put(item, new LinkedList<>());
			}
			elements.get(item).add(i);
		}
	}

	@Override
	public Iterator<T> iterator() {
		return new TupleIterator();
	}
	
	@Override
	public int hashCode() {
		if(hashCode == 1) {
			Iterator<T> it = this.iterator();
			Iterator<Integer> pit = (new Primes()).iterator();
			T item;
			
			while(it.hasNext()) {
				item = it.next();
				hashCode = hashCode * pit.next() + item.hashCode();
			}
		}
		
		return hashCode;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		
		if(obj == null) {
			return false;
		}
		
		if(!(obj instanceof Tuple)) {
			return false;
		}
		
		Tuple<?> other = (Tuple<?>) obj;
		
		if(this.size() != other.size()) {
			return false;
		}
		
		Iterator<T> thisIt = this.iterator();
		Iterator<?> thatIt = other.iterator();
		
		while(thisIt.hasNext() && thatIt.hasNext()) {
			if(!thisIt.next().equals(thatIt.next())) {
				return false;
			}
		}
		
		return true;
	}

	@Override
	public int size() {
		return internalData.length;
	}

	@Override
	public boolean isEmpty() {
		return internalData.length > 0;
	}

	@Override
	public boolean contains(Object obj) {
		return elements.containsKey(obj);
	}

	@Override
	public Object[] toArray() {
		return (Object[]) Arrays.copyOf(internalData, internalData.length);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <E> E[] toArray(E[] a) {
		if(a == null) {
			return (E[]) Arrays.copyOf(internalData, internalData.length);
		}
		
		if (a.length == this.size()) {
			for(int i = 0; i < this.size(); i++) {
				a[i] = (E) this.get(i);
			}
		}
		
		return (E[]) Arrays.copyOf(internalData, internalData.length);
	}

	@Deprecated
	@Override
	public boolean add(T e) {
		throw new UnsupportedOperationException("Tuple does not support the add operation.");
	}

	@Deprecated
	@Override
	public boolean remove(Object o) {
		throw new UnsupportedOperationException("Tuple does not support the remove operation.");
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		for(Object o : c) {
			if(!this.contains(o)) {
				return false;
			}
		}
		return true;
	}

	@Deprecated
	@Override
	public boolean addAll(Collection<? extends T> c) {
		throw new UnsupportedOperationException("Tuple does not support the addAll operation.");
	}

	@Deprecated
	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException("Tuple does not support the removeAll operation.");
	}

	@Deprecated
	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException("Tuple does not support the retainAll operation.");
	}

	@Deprecated
	@Override
	public void clear() {
		throw new UnsupportedOperationException("Tuple does not support the clear operation.");
	}

	@Override
	public T get(int index) {
		return internalData[index];
	}

	@Deprecated
	@Override
	public T set(int index, T element) {
		throw new UnsupportedOperationException("Tuple does not support the set operation.");
	}

	@Deprecated
	@Override
	public void add(int index, T element) {
		throw new UnsupportedOperationException("Tuple does not support the add operation.");
	}
	
	@Deprecated
	@Override
	public boolean addAll(int index, Collection<? extends T> c) {
		throw new UnsupportedOperationException("Tuple does not support the addAll operation.");
	}

	@Deprecated
	@Override
	public T remove(int index) {
		throw new UnsupportedOperationException("Tuple does not support the remove operation.");
	}
	
	@Override
	public int indexOf(Object o) {
		return this.contains(o) ? elements.get(o).get(0) : -1;
	}

	@Override
	public int lastIndexOf(Object o) {
		List<Integer> occurrences = elements.get(o);
		return occurrences == null ? -1 : occurrences.get(occurrences.size()-1);
	}

	@Override
	public ListIterator<T> listIterator() {
		return new TupleListIterator();
	}

	@Override
	public ListIterator<T> listIterator(int index) {
		return new TupleListIterator(index);
	}

	@Override
	public List<T> subList(int fromIndex, int toIndex) {
		if(fromIndex < 0) {
			fromIndex = 0;
		}
		if(toIndex > internalData.length) {
			toIndex = internalData.length;
		}
		
		return new Tuple<T>(Arrays.copyOfRange(internalData, fromIndex, internalData.length));
	}
	
	private class TupleIterator implements Iterator<T> {
		
		private int i = 0;

		@Override
		public boolean hasNext() {
			return i < internalData.length;
		}

		@Override
		public T next() {
			return internalData[i++];
		}
	}
	
	private class TupleListIterator implements ListIterator<T> {
		private int i;
		
		TupleListIterator() {
			this.i = 0;
		}
		
		TupleListIterator(int index) {
			this.i = index;
		}

		@Override
		public boolean hasNext() {
			return i < internalData.length;
		}

		@Override
		public T next() {
			return internalData[i++];
		}

		@Override
		public boolean hasPrevious() {
			return i >= 0;
		}

		@Override
		public T previous() {
			return internalData[i--];
		}

		@Override
		public int nextIndex() {
			return this.hasNext() ? i : i+1;
		}

		@Override
		public int previousIndex() {
			return hasPrevious() ? i-1 : -1;
		}

		@Deprecated
		@Override
		public void remove() {
			throw new UnsupportedOperationException("Tuple iteration does not support the remove operation.");
		}

		@Deprecated
		@Override
		public void set(T e) {
			throw new UnsupportedOperationException("Tuple iteration does not support the set operation.");
		}

		@Deprecated
		@Override
		public void add(T e) {
			throw new UnsupportedOperationException("Tuple iteration does not support the add operation.");
		}
	}
}
