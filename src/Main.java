import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class Main {
    private static final int MAX_DEPTH = 3;

    public static void main(String[] args) {
        System.out.print("Inserisci il seed URL: ");
        String domain = new java.util.Scanner(System.in).nextLine();
        String seedURL = "https://" + domain;

        ArrayList<String> visitedLinks = new ArrayList<>();
        crawl(seedURL, 0, visitedLinks);
    }

    public static void crawl(String currentURL, int currentDepth, ArrayList<String> visitedLinks){
        if (visitedLinks.contains(currentURL)) {
            return;
        }

        visitedLinks.add(currentURL);

        System.out.println("[" + currentDepth + "] " + currentURL);

        if (currentDepth >= MAX_DEPTH) {
            return;
        }

        try {
            //effettua il parsing della pagina con JSoup
            Document document = Jsoup.connect(currentURL).get();
            Elements linksOnPage = document.select("a[href]");

            //scandisce tutti i link trovati nella pagina
            for (Element link : linksOnPage) {
                String nextURL = link.absUrl("href");  // Otteniamo l'URL assoluto

                if (!nextURL.isEmpty() && nextURL.startsWith("https://") && !visitedLinks.contains(nextURL)) {
                    //chiama ricorsivamente la funzione per il link trovato con la profondità incrementata
                    crawl(nextURL, currentDepth + 1, visitedLinks);
                }
            }
        } catch (IOException e) {
            System.err.println("Errore nell'accesso a " + currentURL + ": " + e.getMessage());
        }
    }
}
