package com.wyntalor.services;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wyntalor.controller.AtletaController;
import com.wyntalor.entities.Atleta;
import com.wyntalor.entities.AtletaEstatisticas;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class AtletaService {
    private static final Logger logger = LoggerFactory.getLogger(AtletaController.class);
    private final List<Atleta> atletas;
    private final ObjectMapper objectMapper;
    private HashMap<String, Atleta> atletasMap;
    private static final ObjectMapper objectMapperJson = new ObjectMapper();
    public AtletaService(List<Atleta> atletas, ObjectMapper objectMapper) {
        this.atletas = atletas;
        this.objectMapper = objectMapper;
        this.atletasMap = processarJson();

        //processarJson();
    }

    // aqui realiza a leitura do json para usar como base de dados.
    public HashMap<String, Atleta> processarJson() {
        HashMap<String, Atleta> hashMap = new LinkedHashMap<>();

        try {
            InputStream inputStream = getClass().getResourceAsStream("/json/arquivo_de_atletas.json");
            Atleta[] atletas = objectMapper.readValue(inputStream, Atleta[].class);

            int linha = 1; // Inicia a contagem das linhas

            for (Atleta atleta : atletas) {
                hashMap.put(String.valueOf(linha), atleta);
                linha++; // Incrementa o número da linha para a próxima iteração
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return hashMap;
    }

    public AtletaEstatisticas obterEstatisticasPorPaisEEsporte(String codigoPais, String esporte) {
        //variaveis que vao calcular as medias e contar homens e mulheres
        int quantidadeHomens = 0;
        int quantidadeMulheres = 0;
        double somaIdadesHomens = 0;
        double somaIdadesMulheres = 0;

        //aqui obtem os atletas do país e esporte especifico
        for (Atleta atleta : processarJson().values()) {
            if (atleta.getCountry().equals(codigoPais) && atleta.getSport().equals(esporte)) {
                if (atleta.getGender().equals("male")) {
                    quantidadeHomens++;
                    somaIdadesHomens += atleta.getAge();
                } else if (atleta.getGender().equals("female")) {
                    quantidadeMulheres++;
                    somaIdadesMulheres += atleta.getAge();
                }
            }
        }
        //calculo as medias de idade
        double mediaIdadeHomens = quantidadeHomens > 0 ? somaIdadesHomens / quantidadeHomens : 0;
        double mediaIdadeMulheres = quantidadeMulheres > 0 ? somaIdadesMulheres / quantidadeMulheres : 0;

        //retorna os resultados
        return new AtletaEstatisticas(quantidadeHomens, quantidadeMulheres, mediaIdadeHomens, mediaIdadeMulheres);
    }

    public HashMap<String, Integer> obterQuantidadePessoasPorContinenteEIdade(String esporte, int idade) {
        HashMap<String, Integer> quantidadePorContinente = new HashMap<>();

        //inicializa o mapa com contadores zerados para cada continente
        for (Atleta atleta : processarJson().values()) {
            quantidadePorContinente.put(atleta.getContinent(), 0);
        }

        //conta o numero de pessoas acima da idade por continente
        for (Atleta atleta : processarJson().values()) {
            if (atleta.getSport().equals(esporte) && atleta.getAge() > idade) {
                quantidadePorContinente.put(atleta.getContinent(), quantidadePorContinente.get(atleta.getContinent()) + 1);
            }
        }
        return quantidadePorContinente;
    }

    public Map.Entry<String, Double> obterPaisMaiorMediaBMI(String continente, String genero) {
        // Processa o JSON e obtém a lista de atletas
        Map<String, Atleta> atletasMap = processarJson();
        List<Atleta> atletas = new ArrayList<>(atletasMap.values());

        // Filtra os atletas pelo continente e gênero especificados
        List<Atleta> atletasFiltrados = atletas.stream()
                .filter(a -> a.getContinent().equalsIgnoreCase(continente) && a.getGender().equalsIgnoreCase(genero))
                .collect(Collectors.toList());

        // Calcula a média do BMI para cada país
        Map<String, Double> mediasPorPais = atletasFiltrados.stream()
                .collect(Collectors.groupingBy(Atleta::getCountry,
                        Collectors.averagingDouble(Atleta::getBMI)));

        // Encontra o país com a maior média do BMI
        Optional<Map.Entry<String, Double>> paisComMaiorMediaBMI = mediasPorPais.entrySet().stream()
                .max(Map.Entry.comparingByValue());

        // Retorna o país com a maior média de BMI
        return paisComMaiorMediaBMI.orElse(null);
    }

    // Método para adicionar um atleta na seleção de futebol olímpica masculina do Brasil
    public void adicionarAtletaSelecaoFutebolOlimpica(Atleta novoAtleta) throws IOException {
        // Ler o conteúdo do arquivo JSON existente
        File file = new ClassPathResource("json/arquivo_de_atletas.json").getFile();
        Atleta[] atletas;

        // Verificar se o arquivo existe e se não está vazio
        if (file.exists() && file.length() > 0) {
            // Converter o conteúdo do arquivo em uma lista de objetos Atleta
            atletas = objectMapper.readValue(file, Atleta[].class);
        } else {
            // Se o arquivo estiver vazio ou não existir, inicializar uma lista vazia
            atletas = new Atleta[0];
        }

        // Adicionar o novo atleta à lista de atletas
        List<Atleta> listaAtletas = new ArrayList<>(Arrays.asList(atletas));
        listaAtletas.add(novoAtleta);

        // Escrever a lista atualizada de volta para o arquivo JSON
        objectMapper.writeValue(file, listaAtletas.toArray(new Atleta[0]));
    }

    // Método para listar todos os atletas da seleção de futebol olímpica masculina do Brasil
    public List<Atleta> listarAtletasSelecaoFutebolOlimpicaMasculinaBrasil() {
        // Obter todos os atletas do arquivo JSON
        List<Atleta> todosAtletas = carregarAtletasDoJson();

        // Filtrar apenas os atletas que fazem parte da seleção de futebol olímpica masculina do Brasil
        return todosAtletas.stream()
                .filter(atleta -> atleta.getCountry().equalsIgnoreCase("BR")
                        && atleta.getSport().equalsIgnoreCase("football")
                        && atleta.getGender().equalsIgnoreCase("male"))
                .collect(Collectors.toList());
    }

    // Método para processar o JSON e obter a lista de atletas
    private List<Atleta> carregarAtletasDoJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<List<Atleta>> typeReference = new TypeReference<List<Atleta>>() {};
        InputStream inputStream = TypeReference.class.getResourceAsStream("/json/arquivo_de_atletas.json");

        try {
            return objectMapper.readValue(inputStream, typeReference);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>(); // Retorno de uma lista vazia em caso de erro
        }
    }
    public Set<String> obterTodasLinhasDisponiveis() {
        return processarJson().keySet();
    }
    public boolean editarAtleta(String linha, Atleta atletaAtualizado) {
        logger.info("Editando atleta na linha: {}", linha);
        if (atletasMap.containsKey(linha)) {
            Atleta atletaExistente = atletasMap.get(linha);
            atletaExistente.setContinent(atletaAtualizado.getContinent());
            atletaExistente.setCountry(atletaAtualizado.getCountry());
            atletaExistente.setHeight(atletaAtualizado.getHeight());
            atletaExistente.setWeight(atletaAtualizado.getWeight());
            atletaExistente.setAge(atletaAtualizado.getAge());
            atletaExistente.setGender(atletaAtualizado.getGender());
            atletaExistente.setSport(atletaAtualizado.getSport());
            atletaExistente.setBMI(atletaAtualizado.getBMI());
            atletasMap.put(linha, atletaExistente);
            // Escreve os dados atualizados de volta para o arquivo JSON
            try {
                objectMapper.writeValue(new File("src/main/resources/json/arquivo_de_atletas.json"), atletasMap.values());
                logger.info("Dados atualizados escritos no arquivo JSON");
            } catch (IOException e) {
                logger.error("Erro ao escrever dados atualizados no arquivo JSON: {}", e.getMessage());
                e.printStackTrace();
                return false;
            }
            return true;
        }
        else{
            logger.warn("Atleta não encontrado na linha {}", linha);
            return false;
        }
    }

    public HashMap<String, Atleta> preSelecaoParis() {
        HashMap<String, Atleta> preSelecao = new LinkedHashMap<>();

        for (Map.Entry<String, Atleta> entry : atletasMap.entrySet()) {
            Atleta atleta = entry.getValue();

            // Verificar se o atleta é feminino e tem menos de 20 anos
            if (atleta.getGender().equalsIgnoreCase("female") && atleta.getAge() < 20) {
                // Verificar se o esporte do atleta não é futebol
                if (!atleta.getSport().equalsIgnoreCase("football")) {
                    // Adicionar o atleta à pré-seleção
                    preSelecao.put(entry.getKey(), atleta);
                }
            }
        } List<Atleta> listaPreSelecao = new ArrayList<>(preSelecao.values());
        // Especificar o caminho do arquivo de destino
        String diretorioAtual = System.getProperty("user.dir");
        String caminhoDestino = diretorioAtual + "/src/main/resources/json/paris_feminino.json";
        // Escrever a lista de atletas pré-selecionados no arquivo JSON
        try {
            objectMapper.writeValue(new File(caminhoDestino), listaPreSelecao);
            System.out.println("Os resultados da pré-seleção foram salvos em: " + caminhoDestino);
        } catch (IOException e) {
            System.err.println("Erro ao salvar os resultados da pré-seleção: " + e.getMessage());
        }
        return preSelecao;
    }
    public void adicionarAtletaSelecaoAtletismoBrasil(Atleta novoAtleta) throws IOException {
        // String que representa o caminho de destino do arquivo JSON
        String diretorioAtual = System.getProperty("user.dir");
        String caminhoDestino = diretorioAtual + "/src/main/resources/json/arquivo_de_atletas.json";

        // Ler o conteúdo do arquivo JSON existente
        File file = new File(caminhoDestino);
        Atleta[] atletas;

        // Verificar se o arquivo existe e se não está vazio
        if (file.exists() && file.length() > 0) {
            // Converter o conteúdo do arquivo em uma lista de objetos Atleta
            atletas = objectMapper.readValue(file, Atleta[].class);
        } else {
            // Se o arquivo estiver vazio ou não existir, inicializar uma lista vazia
            atletas = new Atleta[0];
        }

        // Adicionar o novo atleta à lista de atletas
        List<Atleta> listaAtletas = new ArrayList<>(Arrays.asList(atletas));
        listaAtletas.add(novoAtleta);

        // Escrever a lista atualizada de volta para o arquivo JSON
        objectMapper.writeValue(new File(caminhoDestino), listaAtletas);
    }
}