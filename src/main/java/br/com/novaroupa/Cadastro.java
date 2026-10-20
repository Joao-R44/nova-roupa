package br.com.novaroupa;

import java.util.Scanner;

public class Cadastro {
    private String nome;
    private String senha;
    private String email;
    private String cnpj;
    private String opcao;

    public void cadastrarUser(Scanner scanner) {
        System.out.println("Registre seu nome:");
        nome=scanner.nextLine();

        System.out.println("Registre sua senha:");
        senha=scanner.nextLine();

        System.out.println("Registre seu email:");
        email=scanner.nextLine();

        System.out.printf("Olá "+nome+", seja bem vindo. Você gostaria de se cadastrar como vendedor também?");
        opcao=scanner.nextLine();
        
        while(!opcao.equalsIgnoreCase("Sim")&&!opcao.equalsIgnoreCase("Não")&&!opcao.equalsIgnoreCase("Nao")){
            System.out.println("Por favor, digite apenas Sim ou Não:");
            opcao=scanner.nextLine();
        }

        if(opcao.equalsIgnoreCase("Não")||opcao.equalsIgnoreCase("Nao")){
            System.out.println("Entendido, obrigado.");
        }

        else if(opcao.equalsIgnoreCase("Sim")){
            cadastrarVendedor(scanner);
        }
    }

    public void cadastrarVendedor(Scanner scanner){
        System.out.println("Registre seu CNPJ:");
        cnpj=scanner.nextLine();
    }
}
