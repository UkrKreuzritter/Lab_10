package ua.edu.ucu.apps;

public class Main 
{
    public static void main(String [] args)
    {
        CachedDocument newDoc = new CachedDocument(new TimedDocument(new SmartDocument("gs://test_ucu/test.jpg")));
        System.out.println(newDoc.parse());
    }    
}
