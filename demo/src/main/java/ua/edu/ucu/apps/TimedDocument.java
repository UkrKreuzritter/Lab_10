package ua.edu.ucu.apps;

import lombok.Getter;

@Getter
public class TimedDocument implements Document{
    private Document document;
    private String gcsPath;

    public TimedDocument(Document document) 
    {
        this.document = document;
        this.gcsPath = document.getGcsPath();
    }

    public String parse() {
        System.out.println("We are starting");
        long start = System.nanoTime();
        if (!(document instanceof CachedDocument))
            document = new CachedDocument(document);

        String res = document.parse();
    
        System.out.println("Parsing in finished. It took " + (System.nanoTime()-start)/1000000 + " miliseconds");
        return res;

    }
}