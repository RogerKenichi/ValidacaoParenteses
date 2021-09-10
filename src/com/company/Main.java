package com.company;

import java.io.File;
import java.io.FileNotFoundException; // Import this class to handle errors
import java.util.Scanner;
import java.util.ArrayList;
import java.io.FileWriter;
import java.util.Arrays;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        // Verifica se o nome do arquivo de texto foi passado
        if (args.length == 0)
        {
            System.out.println("O nome do arquivo de texto a ser analisado não foi passado como argumento. ");
            return;
        }

        // Obtem o nome do arquivo de texto
        String filename = args[0];
        ArrayList<String> listaLinhas = new ArrayList();

        // Abre o arquivo de texto e o lê linha por linha, validando cada uma delas
        try {
            File objetoFile = new File(filename);
            Scanner leitor = new Scanner(objetoFile);

            while (leitor.hasNextLine()) {
                String linha = leitor.nextLine();
                if (verificarExistenciaDeCaracteres(linha))
                    listaLinhas.add(verificaLinha(linha));
            }
            leitor.close();

            // Salva os resultados num arquivo de texto.
            filename = filename.substring(0, filename.indexOf(".")) + "-check.txt";
            salvarArquivoTexto(listaLinhas, filename);
        }
        catch (FileNotFoundException e) {
            System.out.println("Erro: Arquivo de texto não encontrado");
            System.out.println("      O nome do arquivo deve conter a sua extenção .txt também");
            e.printStackTrace();
        }
    }

    static String verificaLinha(String linha)
    {
        String linhaRetorno = linha;
        Character  caracteresAbertos[] = new Character[] { '(', '[', '{', '<' };
        Character caracteresFechados[] = new Character[] { ')', ']', '}', '>' };

        // Lista de caracteres que foram abertos. Conforme são fechados, são removidos da lista
        ArrayList<Character> listaCaracteresAbertos = new ArrayList();

        // Lê cada caractere da string
        for(int i = 0; i < linha.length(); i++)
        {
            // Verifica se achou um caracter aberto - ( [ { <
            if (verificaDentroArray(linha.charAt(i), caracteresAbertos))
                listaCaracteresAbertos.add(linha.charAt(i)); // Adiciona esse caractere na lista
            // Verifica se achou um caracter fechado - ) ] } >
            if (verificaDentroArray(linha.charAt(i), caracteresFechados))
            {
                // Identifica o index equivalente do caracter fechado para comparar ao último item da lista de caracteres abertos
                int equivalente = Arrays.asList(caracteresFechados).indexOf(linha.charAt(i));
                // Se não havia caracteres abertos ou o caracter aberto no momento é diferente do caracter que fecha, então é inválido
                if (listaCaracteresAbertos.isEmpty() || caracteresAbertos[equivalente] != listaCaracteresAbertos.get(listaCaracteresAbertos.size() - 1))
                    return linhaRetorno + " Invalido";
                else // Mas se for igual, então remove-se o caracter aberto da lista
                    listaCaracteresAbertos.remove(listaCaracteresAbertos.size() - 1);
            }
        }

        // Se no fim das contas não sobraram caracteres abertos, então é válido
        if(listaCaracteresAbertos.isEmpty())
            linhaRetorno = linhaRetorno + " Válido";
        else // Se ainda sobrou algum caractere na lista, então é inválido.
            linhaRetorno = linhaRetorno + " Invalido";

        return linhaRetorno;
    }

    // Verifica se a linha possui algum caractere a ser validado. Se não houver nenhuma, então a linha será ignorada
    static boolean verificarExistenciaDeCaracteres(String linha)
    {
        if (!linha.contains("(") && !linha.contains(")") &&
            !linha.contains("<") && !linha.contains(">") &&
            !linha.contains("{") && !linha.contains("}") &&
            !linha.contains("[") && !linha.contains("]"))
            return false;
        else
            return true;
    }

    // Verifica se uma determinada letra está contida num determinado array
    static boolean verificaDentroArray(char letra, Character array[])
    {
        for(int i = 0; i < array.length; i++)
        {
            if (letra == array[i])
                return true;
        }
        return false;
    }

    static void salvarArquivoTexto(ArrayList<String> lista, String nomeArquivo)
    {
        try {
            FileWriter escrever = new FileWriter(nomeArquivo);
            for (String linha : lista ) {
                escrever.write(linha + "\n");
            }
            escrever.close();
            System.out.println("Arquivo " + nomeArquivo + " escrito com sucesso");
        } catch (IOException e) {
            System.out.println("Erro ao salvar o arquivo");
            e.printStackTrace();
        }
    }
}
