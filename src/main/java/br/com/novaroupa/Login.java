package br.com.novaroupa;

import java.util.ArrayList;
import java.util.Scanner;

public class Login {
    String email;
    String senha;

    Scanner scanner = new Scanner(System.in);
    
    public Cadastro login(ArrayList<Cadastro> cadastros, String email, String senha){
        for(Cadastro cadastro : cadastros){
            if(cadastro.vEmail(email) && cadastro.vSenha(senha)){
                return cadastro;
            }
        } 
                return null;
    }


}
