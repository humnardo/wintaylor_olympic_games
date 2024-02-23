package com.wyntalor.controller;
import com.wyntalor.entities.Atleta;
import com.wyntalor.entities.AtletaEstatisticas;
import com.wyntalor.services.AtletaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.io.IOException;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@RestController
public class AtletaController {
    private static final Logger logger = LoggerFactory.getLogger(AtletaController.class);
    private final AtletaService atletaService;

    @Autowired
    public AtletaController(AtletaService atletaService) {
        this.atletaService = atletaService;
    }

    /**
     * Exercicio 1 aqui retorna todos os atletas com o ID, esse ID sera usado para poder atualizar um atleta
     * @return atletaService.processarJson();
     */
    @GetMapping("/todos")
    public HashMap<String, Atleta> obterTodosAtletas() {
        return atletaService.processarJson();
    }

    /**
     * Exercicio 2 para realizar a consulta, favor seguir o exemplo no postman http://localhost:10001/esportes?codigoPais=RUS
     * @param codigoPais
     * @return esportesPorPais
     */
    @GetMapping("/esportes")
    public Set<String> obterEsportesPorPais(@RequestParam String codigoPais) {
        Set<String> esportesPorPais = new HashSet<>();

        // Obtemos os atletas do serviço
        for (Atleta atleta : atletaService.processarJson().values()) {
            if (atleta.getCountry().equals(codigoPais)) {
                esportesPorPais.add(atleta.getSport());
            }
        }
        return esportesPorPais;
    }
    /**
     * Exercicio 3  para realizar a consulta, favor seguir o exemplo no postman http://localhost:10001/estatisticas?codigoPais=RUS&esporte=boxing
     * @param codigoPais
     * @param esporte
     * @return atletaService.obterEstatisticasPorPaisEEsporte(codigoPais, esporte);
     */
    @GetMapping("/estatisticas")
    public AtletaEstatisticas obterEstatisticasPorPaisEEsporte(
            @RequestParam String codigoPais,
            @RequestParam String esporte
    ) {
        return atletaService.obterEstatisticasPorPaisEEsporte(codigoPais, esporte);
    }

    /**
     * Exercicio 4 para usar esse endpoint seguir o exemplo http://localhost:10001/quantidade-pessoas?esporte=boxing&idade=25
     * @param esporte
     * @param idade
     * @return atletaService.obterQuantidadePessoasPorContinenteEIdade(esporte, idade);
     */
    @GetMapping("/quantidade-pessoas")
    public HashMap<String, Integer> obterQuantidadePessoasPorContinenteEIdade(
            @RequestParam String esporte,
            @RequestParam int idade
    ) {
        return atletaService.obterQuantidadePessoasPorContinenteEIdade(esporte, idade);
    }

    /**
     * Exercicio 5 media de BM por continente
     * @param continente
     * @param genero
     * @return atletaService.obterPaisMaiorMediaBMI(continente, genero);
     */
    @GetMapping("/pais-maior-media-bmi")
    public Map.Entry<String, Double> obterPaisMaiorMediaBMIContinente(
            @RequestParam String continente,
            @RequestParam String genero
    ) {
        return atletaService.obterPaisMaiorMediaBMI(continente, genero);
    }

    /**
     * Exercicio 6 para adicionar um atleta na seleção do brasil, seguir o json
     * {
     *   "height": 180,
     *   "weight": 75,
     *   "BMI": 23.1,
     *   "age": 150,
     *   "sport": "Football"
     * }
     * @param atleta
     * @return ResponseEntity
     * @throws IOException
     */
    @PostMapping("/adicionar-atleta-selecao-futebol")
    public ResponseEntity<String> adicionarAtletaSelecaoFutebolOlimpica(@RequestBody Atleta atleta) throws IOException {
        // Defina o continente, país e gênero conforme necessário
        atleta.setContinent("North America"); // Não entendi o motivo de estar errado isso, mas so segui o JSON e o enunciado.
        atleta.setCountry("BR");
        atleta.setGender("Male");

        // Chame o serviço para adicionar o atleta à seleção de futebol olímpica
        atletaService.adicionarAtletaSelecaoFutebolOlimpica(atleta);

        return ResponseEntity.ok("Atleta adicionado com sucesso à seleção de futebol olímpica do Brasil.");
    }

    /**
     * Exercicio 7 retorna os atletas de futebol da selecao do rbasil
     * @return
     */
    @GetMapping("/atletas-brasil")
    public List<Atleta> listarAtletasSelecaoFutebolOlimpicaMasculinaBrasil() {
        return atletaService.listarAtletasSelecaoFutebolOlimpicaMasculinaBrasil();
    }

    /**
     * Exercicio 8 aqui utilizamos a linha para poder atualizar um atleta juntamente com o json
     * @param linha
     * @param atletaAtualizado
     * @return HttpStatus
     */
    @PutMapping("/{linha}")
    public HttpStatus editarAtleta(
            @PathVariable String linha,
            @RequestBody Atleta atletaAtualizado) {

        Set<String> linhasDisponiveis = atletaService.obterTodasLinhasDisponiveis();

        if (linhasDisponiveis.contains(linha)) {
            boolean sucesso = atletaService.editarAtleta(linha, atletaAtualizado);
            return sucesso ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        } else {
            return HttpStatus.NOT_FOUND;
        }
    }

    /**
     * exercicio 9
     * @return
     */
    @GetMapping("/paris")
    public HashMap<String, Atleta> preSelecaoParis() {
        return atletaService.preSelecaoParis();
    }

}