package br.com.brasileirao.api.util;

import br.com.brasileirao.api.dto.PartidaGoogleDTO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ScrapingUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScrapingUtil.class);

    private static final String BASE_URL_GOOGLE = "https://www.google.com/search?q=";
    private static final String COMPLEMENTO_URL_GOOGLE=  "&hl=pt-BR";

    private static final String CASA = "casa";
    private static final  String VISITANTE = "visitante";

    public static void main(String[] args) {
        String url = BASE_URL_GOOGLE+ "Brasil+x+Croácia" + COMPLEMENTO_URL_GOOGLE;

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

            if (statusPartida != StatusPartida.PARTIDA_NAO_INICIADA) {
                String tempoPartida = obtemTempoPartida(document);
                LOGGER.info("Tempo partida: {}", tempoPartida);

                Integer placarEquipeCasa = recuperaPlacarEquipeCasa(document);
                LOGGER.info("Placar equipe casa: {}", placarEquipeCasa);

                Integer placarEquipeVisitante = recuperaPlacarEquipeVisitante(document);
                LOGGER.info("Placar equipe casa: {}", placarEquipeVisitante);

                String golsEquipeCasa = recuperaGolsEquipeCasa(document);
                LOGGER.info("Gols equipe casa: {}", golsEquipeCasa);

                String golsEquipeVisitante = recuperaColsEquipeVisitante(document);
                LOGGER.info("Gols equipe casa: {}", golsEquipeVisitante);

                Integer placarEstendidoEquipeCasa = buscarPenalidades(document, CASA);
                LOGGER.info("Placar estendido equipe casa: {}", placarEstendidoEquipeCasa);

                Integer placarEstendidoEquipeVisitante = buscarPenalidades(document, VISITANTE);
                LOGGER.info("Placar estendido equipe visitante: {}", placarEstendidoEquipeVisitante);
            }
            
            String nomeEquipeCasa = recuperaNomeEquipeCasa(document);
            LOGGER.info("Nome Equipe Casa: {}", nomeEquipeCasa);

            String nomeEquipeVisitante = recuperaNomeEquipeVisitante(document);
            LOGGER.info("Nome Equipe Casa: {}", nomeEquipeVisitante);

            String logoEquipeCasa = recuperaLogoEquipeCasa(document);
            LOGGER.info("Logo equipe casa: {}", logoEquipeCasa);

            String logoEquipeVisitante = recuperaLogoEquipeVisitante(document);
            LOGGER.info("Logo equipe Visitante: {}", logoEquipeVisitante);


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

    public String recuperaNomeEquipeCasa(Document document) {
        Element elemento = document.selectFirst("div[class=imso_mh__first-tn-ed imso_mh__tnal-cont imso-tnol]");
        return elemento.select("span").text();
    }

    public String recuperaNomeEquipeVisitante(Document document) {
        Element elemento = document.selectFirst("div[class=imso_mh__second-tn-ed imso_mh__tnal-cont imso-tnol]");
        return elemento.select("span").text();
    }

    public String recuperaLogoEquipeCasa(Document document) {
        Element elemento = document.selectFirst("div[class=imso_mh__first-tn-ed imso_mh__tnal-cont imso-tnol]");
        return elemento.select("img[class=imso_btl__mh-logo]").attr("src");
    }

    public String recuperaLogoEquipeVisitante(Document document) {
        Element elemento = document.selectFirst("div[class=imso_mh__second-tn-ed imso_mh__tnal-cont imso-tnol]");
        return elemento.select("img[class=imso_btl__mh-logo]").attr("src");
    }

    public Integer recuperaPlacarEquipeCasa(Document document) {
        String placarEquipe = document.selectFirst("div[class=imso_mh__l-tm-sc imso_mh__scr-it imso-light-font]").text();
        return formataPlacarStringInteger(placarEquipe);
    }
    public Integer recuperaPlacarEquipeVisitante(Document document) {
        String placarEquipe = document.selectFirst("div[class=imso_mh__r-tm-sc imso_mh__scr-it imso-light-font]").text();
        return formataPlacarStringInteger(placarEquipe);
    }
    public String recuperaGolsEquipeCasa(Document document){
        List<String> golsEquipe = new ArrayList<>();

        Elements elementos = document.select("div[class=imso_gs__tgs imso_gs__left-team]")
                .select("div[class=imso_gs__gs-r]");
        for (Element e: elementos) {
            String infoGol = e.select("div[class=imso_gs__gs-r]").text();
            golsEquipe.add(infoGol);
        }

        return String.join(", ", golsEquipe);
    }

    public String recuperaColsEquipeVisitante(Document document) {
        List<String> golsEquipe = new ArrayList<>();
        Elements elementos = document.select("div[class=imso_gs__tgs imso_gs__right-team]")
                .select("div[class=imso_gs__gs-r]");
        for (Element e : elementos) {
            String infoGol = e.select("div[class=imso_gs__gs-r]").text();
            golsEquipe.add(infoGol);
        }
        return String.join(", ", golsEquipe);
    }

    public Integer buscarPenalidades(Document document, String tipoEquipe) {
        boolean isPenalidade = document.select("div[class=imso_mh_s__psn-sc]").isEmpty();
        if (!isPenalidade) {
            String penalidades = document.select("div[class=imso_mh_s__psn-sc]").text();
            String penalidadesCompleta = penalidades.substring(0,5).replace(" ", "");
            String[] divisao = penalidadesCompleta.split("-");
            return tipoEquipe.equals(CASA) ? formataPlacarStringInteger(divisao[0]) : formataPlacarStringInteger(divisao[1]);
        }
        return null;
    }

    public Integer formataPlacarStringInteger(String placar) {
        Integer valor;
        try {
            valor = Integer.parseInt(placar);
        } catch (Exception e) {
            valor = 0;
        }
        return valor;
    }

}
