package ua.edu.ucu.apps;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import lombok.Getter;
import lombok.SneakyThrows;

public class CachedDocument implements Document {
    @Getter
    private String gcsPath;
    private Document document;
    private DatabaseConnection dbConnection;
    private Connection connection;

    public CachedDocument(Document document) {
        this.document = document;
        this.gcsPath = document.getGcsPath();
        this.dbConnection = DatabaseConnection.getInstance();
        this.connection = dbConnection.getConnection();
    }

    @SneakyThrows
    public String parse() 
    {
        if (document instanceof TimedDocument)
            document.parse();

        String queryFind = "SELECT document_text FROM documents WHERE gcs_path = ?";
        PreparedStatement preparedStatementFind = connection.prepareStatement(queryFind);
        preparedStatementFind.setString(1, gcsPath);
        ResultSet resultSet = preparedStatementFind.executeQuery();
        if (resultSet.next())
            return "Reading from database:\n\n"+ resultSet.getString("document_text");
        String documentText = document.parse();
        String queryWrite = "INSERT INTO documents (gcs_path, document_text) VALUES (?, ?)";
        PreparedStatement preparedStatementWrite = connection.prepareStatement(queryWrite);
        preparedStatementWrite.setString(1, gcsPath);
        preparedStatementWrite.setString(2, documentText);
        preparedStatementWrite.executeUpdate();

        return "Proccesing file via google vision OCR and saving it to database: \n\n"+ documentText;
    }
}