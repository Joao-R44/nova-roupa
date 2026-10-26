package br.com.novaroupa;

public class Cadastro {
    private String nome;
    private String senha;
    private String email;
    private String cnpj;

    public void cadastrarUsuario(String nome, String senha, String email) {
        this.nome= nome;
        this.senha=senha;
        this.email=email;
    }

    public void cadastrarVendedor(String cnpj){
        this.cnpj=cnpj;
    }

    public String getNome(){
        return this.nome;
    }

    public String getEmail(){
        return this.email;
    }

    public String getSenha(){
        return this.senha;
    }

    public String getCnpj(){
        return this.cnpj;
    }

    public boolean vEmail(String email){
        if(this.email.equals(email)){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean vSenha(String senha){
        if(this.senha.equals(senha)){
            return true;
        }
        else{
            return false;
        } 
    }

    public boolean vCnpj(String cnpj){
        if(this.cnpj.equals(cnpj)){
            return true;
        }

        else{
            return false;
        }
    }
}
