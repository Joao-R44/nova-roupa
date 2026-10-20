package br.com.novaroupa;

import java.util.Scanner;

public class Main{
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        Cadastro cadastro = new Cadastro();
        
        int opcao;
        boolean sair=false;

        while(sair==false){
        System.out.println("Bem vindo ao Roupa Nova. Escolha uma opção para executar:\n1-Cadastrar\n2-Login\n3-Sair");
        opcao=scanner.nextInt();
        scanner.nextLine();

        while(opcao<1||opcao>3){
            System.out.println("Opção inválida, escolha novamente:");
            opcao=scanner.nextInt();
            scanner.nextLine();
        }
        if(opcao==1){
            cadastro.cadastrarUser(scanner);
            sair=true;
        }
        if(opcao==2){
            System.out.println("Ainda em desenvolvimento :/");
        }
        if(opcao==3){
            sair=true;
        }
    }
        scanner.close();
    }
}