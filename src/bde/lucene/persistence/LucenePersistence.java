package bde.lucene.persistence;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import bde.dao.Persistence;
import bde.iterator.OperatorLucene;
import bde.lucene.core.Indexer;
import bde.lucene.core.LuceneResult;
import bde.lucene.core.Searcher;

public class LucenePersistence implements Persistence{

	private String indexDir;
	private String dataDir;
	
	private Indexer indexer;
	private Searcher searcher;
	
	public LucenePersistence() {
		
	}
	
	public LucenePersistence(String indexDir, String dataDir) {
		this.indexDir = indexDir;
		this.dataDir = dataDir;
	}
	
	@Override
	public void dataInit() {
		try {
			indexer = new Indexer(indexDir);
			searcher = new Searcher(indexDir);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	public void createIndex() throws IOException {
		indexer = new Indexer(indexDir);
		
		int numIndexed;
		long startTime = System.currentTimeMillis();	
		
		numIndexed = indexer.createIndex(dataDir, new TextFileFilter());
		long endTime = System.currentTimeMillis();
		indexer.close();
		
		System.out.println(numIndexed+" File indexed, time taken: "+(endTime-startTime)+" ms");		
	}
		   
	public OperatorLucene search(String searchQuery) throws IOException, ParseException {
		OperatorLucene listLuceneResult = new OperatorLucene();
		
		searcher = new Searcher(indexDir);
		
		TopDocs hits = searcher.search(searchQuery);
		   		
		for(ScoreDoc scoreDoc : hits.scoreDocs) {
			Document doc = searcher.getDocument(scoreDoc);
			
			String fileName = doc.get(LuceneConstants.FILE_NAME);
			String id = fileName.split(".txt")[0];
			
			LuceneResult luceneResult = new LuceneResult(id, scoreDoc.score); 
			listLuceneResult.add(luceneResult);
		}
		
		return listLuceneResult;
	}
	
	public void deletIndexFile() {
		File dir = new File(indexDir);
		
		if(dir.isDirectory() == false) {
			System.out.println("Not a directory. Do nothing");
			return;
		}
		
		File[] listFiles = dir.listFiles();
		
		for(File file : listFiles){
			file.delete();
		}		
	}

	public String getIndexDir() {
		return indexDir;
	}

	public void setIndexDir(String indexDir) {
		this.indexDir = indexDir;
	}

	public String getDataDir() {
		return dataDir;
	}

	public void setDataDir(String dataDir) {
		this.dataDir = dataDir;
	}

	public Indexer getIndexer() {
		return indexer;
	}

	public void setIndexer(Indexer indexer) {
		this.indexer = indexer;
	}

	public Searcher getSearcher() {
		return searcher;
	}

	public void setSearcher(Searcher searcher) {
		this.searcher = searcher;
	}
}