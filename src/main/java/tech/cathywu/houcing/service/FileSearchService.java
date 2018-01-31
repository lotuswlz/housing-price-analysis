package tech.cathywu.houcing.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FileSearchService {

    public void createIndex(String announcementId, List<String> fileNames) throws IOException {
        IndexWriterConfig indexConfig = new IndexWriterConfig(new CJKAnalyzer());
        indexConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

        IndexWriter indexWriter = new IndexWriter(getIndexDirectory(announcementId), indexConfig);
        indexWriter.addDocuments(fileNames.stream().map(fileName -> {
            Document document = new Document();
            document.add(new TextField("announcementId", announcementId, Field.Store.YES));
            document.add(new TextField("realEstateName", fileName, Field.Store.YES));
            return document;
        }).collect(Collectors.toList()));
        indexWriter.close();
    }

    public List<String> searchNames(String announcementId, String realEstateName) throws IOException, ParseException {
        IndexReader reader = DirectoryReader.open(getIndexDirectory(announcementId));
        try {
            final IndexSearcher searcher = new IndexSearcher(reader);

            QueryParser parser = new QueryParser("realEstateName", new CJKAnalyzer());
            Query query = parser.parse(realEstateName);

            TopDocs result = searcher.search(query, 3);
            ScoreDoc[] scoreDocs = result.scoreDocs;
            List<String> fileNames = Arrays.stream(scoreDocs).map(d -> {
                try {
                    return searcher.doc(d.doc);
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                    return null;
                }
            }).map(d -> d.get("realEstateName")).collect(Collectors.toList());

            if (fileNames.size() > 1) {
                List<String> exactMatchedNames = fileNames.stream().filter(fn -> fn.contains(realEstateName)).collect(Collectors.toList());
                if (!exactMatchedNames.isEmpty()) {
                    return exactMatchedNames;
                }
            }

            return fileNames;
        } finally {
            reader.close();
        }
    }

    public FSDirectory getIndexDirectory(String subPath) throws IOException {
        String indexPath = "index" + File.separator + subPath;
        File dir = new File(indexPath);
        if (!dir.exists()) {
            dir.mkdirs();
            dir.setReadable(true);
            dir.setWritable(true);
        }
        return FSDirectory.open(Paths.get(indexPath));
    }
}
