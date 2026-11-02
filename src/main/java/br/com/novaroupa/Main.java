package br.com.novaroupa;

import java.util.Scanner;
import java.util.ArrayList;

public class Main{
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        ArrayList <Cadastro> cadastros= new ArrayList<>();
        
        int opcao;
        int opcaoCad;
        boolean sair=false;
        boolean senhaRepetida=false;
        boolean emailRepetido = false;
        boolean cnpjRepetido= false;

        while(sair==false){
            System.out.println("Bem vindo ao Roupa Nova. Escolha uma opção para executar:\n1-Cadastrar\n2-Login\n3-Sair\n4-Listar Usuários");
            opcao=scanner.nextInt();
            scanner.nextLine();

        while(opcao<1||opcao>4){
            System.out.println("Opção inválida, escolha novamente:");
            opcao=scanner.nextInt();
            scanner.nextLine();
        }

        if(opcao==1){
            System.out.println("Registre seu nome:");
            String nome=scanner.nextLine();

            System.out.println("Registre sua senha:");
            String senha= scanner.nextLine();

            for(Cadastro verificarCad : cadastros){
                if(verificarCad.vSenha(senha)){
                    senhaRepetida=true;
                    break;
                    }
                }
                    while(senhaRepetida==true){
                        System.out.printf("Senha inválida, por favor digite novamente:");
                        senha=scanner.nextLine();

                        senhaRepetida=false;
                        
                        for(Cadastro verificarCad : cadastros){
                        if(verificarCad.vSenha(senha)){
                        senhaRepetida=true;
                        break;
                    }
                }
            }
            
            System.out.println("Registre seu email:");
            String email= scanner.nextLine();

                for(Cadastro verificarCad : cadastros){
                if(verificarCad.vEmail(email)){
                    emailRepetido=true;
                    break;
                    }
                }
                    while(emailRepetido==true){
                        System.out.printf("Email inválido, por favor digite novamente:");
                        email=scanner.nextLine();

                        emailRepetido=false;
                        
                        for(Cadastro verificarCad : cadastros){
                        if(verificarCad.vEmail(email)){
                        emailRepetido=true;
                        break;
                    } 
            }
                    }

            Cadastro cadNovo= new Cadastro();
            cadNovo.cadastrarUsuario(nome, senha, email);
            cadastros.add(cadNovo);

            System.out.println("Olá "+nome+" , seja bem vindo. Gostaria de se cadastrar como um vendedor? Digite 0 para sim ou 1 para não:");

            opcaoCad=scanner.nextInt();
            scanner.nextLine();

            while(opcaoCad<0||opcaoCad>1){
                System.out.println("Opção inválida, por favor tente novamente:");
                opcaoCad=scanner.nextInt();
                scanner.nextLine();
            }
            
            if(opcaoCad==0){
                System.out.println("Digite seu CNPJ:");
                String cnpj=scanner.nextLine();

                for(Cadastro verificarCad : cadastros){
                    if(verificarCad.vCnpj(cnpj)==true){
                        cnpjRepetido=true;
                        break;
                    }
                }

                while(cnpjRepetido==true){
                    System.out.printf("CNPJ inválido, por favor digite novamente:");
                    cnpj=scanner.nextLine();

                    cnpjRepetido=false;

                    for(Cadastro verificarCad : cadastros){
                        if(verificarCad.vCnpj(cnpj)==true){
                            cnpjRepetido=true;
                        }
                    }
                }

                cadNovo.cadastrarVendedor(cnpj);

                System.out.println("Retornando ao menu principal...");
            }
            else{
                System.out.println("Entendido. Retornando ao menu principal...");
            }
        }

        if(opcao==2){
            if(cadastros.isEmpty()){
                System.out.println("Sem usuários cadastrados!");
                continue;
            }
            Login login = new Login();
            System.out.println("Digite seu email:");
            String email=scanner.nextLine();

            System.out.println("Digite sua senha:");
            String senha=scanner.nextLine();

            Cadastro usuarioLoggado = login.login(cadastros,email,senha);

            while(usuarioLoggado==null){
                System.out.println("Erro! Email ou senha incorretos.");
                 
                System.out.println("Digite seu email:");
                email=scanner.nextLine();

                 System.out.println("Digite sua senha:");
                senha=scanner.nextLine();
            }

            System.out.println("Loggado com sucesso! Bem vindo"+ usuarioLoggado.getNome()+"!");
           
        }
        
        if(opcao==3){
            System.out.println("Saindo...");
            sair=true;
        }

        if(opcao==4){
            for(Cadastro listarCadastro : cadastros){
                System.out.println(listarCadastro.getNome());
                System.out.println(listarCadastro.getEmail()+"\n");
            }
        }
    }
        scanner.close();
    }
}