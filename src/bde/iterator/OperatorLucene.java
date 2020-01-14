package bde.iterator;

import java.io.IOException;

import org.apache.lucene.queryparser.classic.ParseException;

import bde.lucene.persistence.LucenePersistence;

public class OperatorLucene implements Iterator{
	
	private Node head;
	private String query;
	
	public OperatorLucene() {
	
	}
	
	public OperatorLucene(String query) {
		this.query = query;
	}
	
	public OperatorLucene(Node head, String query) {
		this.head = head;
		this.query = query;
	}
	
	@Override
	public void init() {
		try {
			head = LucenePersistence.search(query).head;
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		} 
	}

	@Override
	public boolean next() {
		Node next = head.getNext();
		if(next == null) {
			return false;
		}
		else {
			head = next;
			return true;
		}
	}

	@Override
	public void add(Node node) {
		if(head == null) {
			head = node;
		}
		else {
			Node current = head;
			while (current.getNext() != null) {
				current = current.getNext();
			}
			current.setNext(node);
		}
	}

	@Override
	public int getColumnCount() {
		return head.getSize();
	}

	@Override
	public String getString(int columnIndex) throws Exception{
		return head.getColumnAt(columnIndex);
	}
	
	public Node exists(String id) {
		Node exists = null;
		Node current = head;
		
		while(current != null && exists == null) {
			exists = (current.getData()[0].equals(id)) ? current : null;
			current = current.getNext();
		}
		
		return exists;
	}
}