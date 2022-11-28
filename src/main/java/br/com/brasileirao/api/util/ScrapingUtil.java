package br.com.brasileirao.api.util;

import br.com.brasileirao.api.dto.PartidaGoogleDTO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ScrapingUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScrapingUtil.class);

    private static final String BASE_URL_GOOGLE = "https://www.google.com/search?q=";
    private static final String COMPLEMENTO_URL_GOOGLE=  "&hl=pt-BR";

    public static void main(String[] args) {
        String url = BASE_URL_GOOGLE+ "Bélgica+x+Marrocos" + COMPLEMENTO_URL_GOOGLE;

        ScrapingUtil scraping = new ScrapingUtil();
        scraping.obtemInformacoesPartida(url);
    }
    public PartidaGoogleDTO obtemInformacoesPartida(String url){
        PartidaGoogleDTO partida = new PartidaGoogleDTO();

        Document document = null;
        try {
            document = Jsoup.connect(url).get();
            String title = document.title();
            LOGGER.info("Título da pagina: {}", title);

            StatusPartida statusPartida = obtemStatusPartida(document);
            LOGGER.info("Status partida: {}", statusPartida);

            String tempoPartida = obtemTempoPartida(document);
            LOGGER.info("Tempo partida: {}", tempoPartida);
        } catch (IOException e) {
            LOGGER.error("ERRO AO TENTAR CONECTAR NO GOOGLE COM JSOUP -> {}", e.getMessage());
            e.printStackTrace();
        }
        return partida;
    }

    public StatusPartida obtemStatusPartida(Document document) {
        StatusPartida statusPartida = StatusPartida.PARTIDA_NAO_INICIADA;
        boolean isTempoPartida = document.select("span[class=imso_mh__ft-mtch imso-medium-font imso_mh__ft-mtchc]").isEmpty();
        if (!isTempoPartida) {
            String tempoPartida = document.select("span[class=imso_mh__ft-mtch imso-medium-font imso_mh__ft-mtchc]").first().text();
            statusPartida = StatusPartida.PARTIDA_ENCERRADA;
        }
        return statusPartida;
    }
    public String obtemTempoPartida(Document document) {
        String tempoPartida = null;
        boolean isTempoPartida = document.select("span[class=imso_mh__ft-mtch imso-medium-font imso_mh__ft-mtchc]").isEmpty();
        if (!isTempoPartida) {
            tempoPartida = document.select("span[class=imso_mh__ft-mtch imso-medium-font imso_mh__ft-mtchc]").first().text();
        }
        return corrigeTempoPartida(tempoPartida);
    }

    public String corrigeTempoPartida(String tempo) {
        if (tempo.contains("'")) {
            return tempo.replace(" ","").replace("'", " min");
        }  else {
            return tempo;
        }
    }
}
