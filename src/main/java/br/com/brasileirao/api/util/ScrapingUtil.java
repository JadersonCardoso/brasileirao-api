package br.com.brasileirao.api.util;

import br.com.brasileirao.api.dto.PartidaGoogleDTO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ScrapingUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScrapingUtil.class);

    private static final String BASE_URL_GOOGLE = "https://www.google.com/search?q=";
    private static final String COMPLEMENTO_URL_GOOGLE=  "&hl=pt-BR";

    private static final String SPAN_TEMPO_PARTIDA = "span[class=imso_mh__ft-mtch imso-medium-font imso_mh__ft-mtchc]";

    private static final String DIV_PLACAR_EQUIPE_CASA="div[class=imso_mh__l-tm-sc imso_mh__scr-it imso-light-font]";
    private static final String DIV_PLACAR_EQUIPE_VISITANTE = "div[class=imso_mh__r-tm-sc imso_mh__scr-it imso-light-font]";

    private static final String DIV_GOLS_EQUIPE_CASA = "div[class=imso_gs__tgs imso_gs__left-team]";
    private static final String DIV_GOLS_EQUIPE_VISITANTE = "div[class=imso_gs__tgs imso_gs__right-team]";

    private static  final String ITEM_GOL = "div[class=imso_gs__gs-r]";
    private static final String DIV_PENALIDADES = "div[class=imso_mh_s__psn-sc]";

    private String DIV_DADOS_EQUIPE_CASA = "div[class=imso_mh__first-tn-ed imso_mh__tnal-cont imso-tnol]";
    private String DIV_DADOS_EQUIPE_VISITANTE = "div[class=imso_mh__second-tn-ed imso_mh__tnal-cont imso-tnol]" ;

    private static final String ITEM_LOGO= "img[class=imso_btl__mh-logo]";
    private static final String CASA = "casa";
    private static final  String VISITANTE = "visitante";

    public PartidaGoogleDTO obtemInformacoesPartida(String url){
        PartidaGoogleDTO partida = new PartidaGoogleDTO();

        Document document = null;
        try {
            document = Jsoup.connect(url).get();
            String title = document.title();
            LOGGER.info("Título da pagina: {}", title);

            StatusPartida statusPartida = obtemStatusPartida(document);
            partida.setStatusPartida(statusPartida.toString());
            LOGGER.info("Status partida: {}", statusPartida);

            if (statusPartida != StatusPartida.PARTIDA_NAO_INICIADA) {
                String tempoPartida = obtemTempoPartida(document);
                partida.setTempoPartida(tempoPartida);
                LOGGER.info("Tempo partida: {}", tempoPartida);

                Integer placarEquipeCasa = recuperaPlacarEquipe(document, DIV_PLACAR_EQUIPE_CASA);
                partida.setPlacarEquipeCasa(placarEquipeCasa);
                LOGGER.info("Placar equipe casa: {}", placarEquipeCasa);

                Integer placarEquipeVisitante = recuperaPlacarEquipe(document, DIV_PLACAR_EQUIPE_VISITANTE);
                partida.setPlacarEquipeVisitante(placarEquipeVisitante);
                LOGGER.info("Placar equipe casa: {}", placarEquipeVisitante);

                String golsEquipeCasa = recuperaGolsEquipe(document, DIV_GOLS_EQUIPE_CASA);
                partida.setGolsEquipeCasa(golsEquipeCasa);
                LOGGER.info("Gols equipe casa: {}", golsEquipeCasa);

                String golsEquipeVisitante = recuperaGolsEquipe(document, DIV_GOLS_EQUIPE_VISITANTE);
                partida.setGolsEquipeVisitante(golsEquipeVisitante);
                LOGGER.info("Gols equipe casa: {}", golsEquipeVisitante);

                Integer placarEstendidoEquipeCasa = buscarPenalidades(document, CASA);
                partida.setPlacarEstendidoEquipeCasa(placarEstendidoEquipeCasa.toString());
                LOGGER.info("Placar estendido equipe casa: {}", placarEstendidoEquipeCasa);

                Integer placarEstendidoEquipeVisitante = buscarPenalidades(document, VISITANTE);
                partida.setPlacarEstendidoEquipeVisitante(placarEstendidoEquipeVisitante.toString());
                LOGGER.info("Placar estendido equipe visitante: {}", placarEstendidoEquipeVisitante);
            }
            
            String nomeEquipeCasa = recuperaNomeEquipe(document, DIV_DADOS_EQUIPE_CASA);
            partida.setNomeEquipeCasa(nomeEquipeCasa);
            LOGGER.info("Nome Equipe Casa: {}", nomeEquipeCasa);

            String nomeEquipeVisitante = recuperaNomeEquipe(document, DIV_DADOS_EQUIPE_VISITANTE);
            partida.setNomeEquipeVisitante(nomeEquipeVisitante);
            LOGGER.info("Nome Equipe Casa: {}", nomeEquipeVisitante);

            String logoEquipeCasa = recuperaLogoEquipe(document, DIV_DADOS_EQUIPE_CASA);
            partida.setUrlLogoEquipeCasa(logoEquipeCasa);
            LOGGER.info("Logo equipe casa: {}", logoEquipeCasa);

            String logoEquipeVisitante = recuperaLogoEquipe(document, DIV_DADOS_EQUIPE_VISITANTE);
            partida.setUrlLogoEquipeVisitante(logoEquipeVisitante);
            LOGGER.info("Logo equipe Visitante: {}", logoEquipeVisitante);

            return partida;
        } catch (IOException e) {
            LOGGER.error("ERRO AO TENTAR CONECTAR NO GOOGLE COM JSOUP -> {}", e.getMessage());
        }
        return null;
    }

    public StatusPartida obtemStatusPartida(Document document) {
        StatusPartida statusPartida = StatusPartida.PARTIDA_NAO_INICIADA;
        boolean isTempoPartida = document.select(SPAN_TEMPO_PARTIDA).isEmpty();
        if (!isTempoPartida) {
            String tempoPartida = document.select(SPAN_TEMPO_PARTIDA).first().text();
            statusPartida = StatusPartida.PARTIDA_ENCERRADA;
        }
        return statusPartida;
    }
    public String obtemTempoPartida(Document document) {
        String tempoPartida = null;
        boolean isTempoPartida = document.select(SPAN_TEMPO_PARTIDA).isEmpty();
        if (!isTempoPartida) {
            tempoPartida = document.select(SPAN_TEMPO_PARTIDA).first().text();
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

    public String recuperaNomeEquipe(Document document, String itemHtml) {
        Element elemento = document.selectFirst(itemHtml);
        return elemento.select("span").text();
    }

    public String recuperaLogoEquipe(Document document, String itemHtml) {
        Element elemento = document.selectFirst(itemHtml);
        return elemento.select(ITEM_LOGO).attr("src");
    }

    public Integer recuperaPlacarEquipe(Document document, String itemHtml) {
        String placarEquipe = document.selectFirst(itemHtml).text();
        return formataPlacarStringInteger(placarEquipe);
    }

    public String recuperaGolsEquipe(Document document, String itemHtml){
        List<String> golsEquipe = new ArrayList<>();

        Elements elementos = document.select(itemHtml)
                .select(ITEM_GOL);
        for (Element e: elementos) {
            String infoGol = e.select(ITEM_GOL).text();
            golsEquipe.add(infoGol);
        }

        return String.join(", ", golsEquipe);
    }


    public Integer buscarPenalidades(Document document, String tipoEquipe) {
        boolean isPenalidade = document.select(DIV_PENALIDADES).isEmpty();
        if (!isPenalidade) {
            String penalidades = document.select(DIV_PENALIDADES).text();
            String penalidadesCompleta = penalidades.substring(0, 5).replace(" ", "");
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

    public String montaUrlGoogle(String nomeEquipeCasa, String nomeEquipeVisitante) {
        try {
            String equipeCasa = nomeEquipeCasa.replace(" ", "+").replace("-","+");
            String equipeVisitante = nomeEquipeVisitante.replace(" ", "+").replace("-","+");
            return BASE_URL_GOOGLE + equipeCasa + "+x+" + equipeVisitante +COMPLEMENTO_URL_GOOGLE;
        } catch (Exception ex) {
            LOGGER.error("ERRO: {}", ex.getMessage());
        }
        return null;
    }

}
